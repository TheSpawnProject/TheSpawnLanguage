package net.programmer.igoodie.tsl.logging;

import net.programmer.igoodie.goodies.util.FileUtils;
import net.programmer.igoodie.tsl.exception.TSLInternalError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class TSLLogHandler extends Handler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Options
    private final File logsDir;
    private final String logName;
    private final TSLLogFormatter formatter;
    private final List<Consumer<String>> hooks;
    private int historyLimit;

    // Target stuff
    private File targetFile;
    private PrintWriter targetPrintWriter;

    public TSLLogHandler(File logsDir, String logName) {
        this.logsDir = logsDir;
        this.logName = logName;
        this.formatter = new TSLLogFormatter(logName);
        this.hooks = new LinkedList<>();
        this.historyLimit = 10;
        changeTargetFile(outputFileByDate(new Date()));
    }

    public TSLLogHandler hookConsoleLog() {
        return hookConsumer(System.out::println);
    }

    public TSLLogHandler hookConsumer(Consumer<String> messageConsumer) {
        this.hooks.add(messageConsumer);
        return this;
    }

    public TSLLogHandler historyLimit(int historyLimit) {
        this.historyLimit = historyLimit;
        removeOldLogs();
        return this;
    }

    /* -------------------- */

    private String outputFileByDate(Date date) {
        return String.format("%s-%s.log", logName, DATE_FORMAT.format(date));
    }

    private void changeTargetFile(String fileName) {
        try {
            if (this.targetFile != null) {
                close(); // Close previous file
            }

            this.targetFile = new File(logsDir, fileName);
            FileUtils.createFileIfAbsent(targetFile);
            this.targetPrintWriter = new PrintWriter(new FileOutputStream(targetFile, true));

            removeOldLogs();

        } catch (FileNotFoundException e) {
            throw new TSLInternalError("Log file was not found...").causedBy(e);
        }
    }

    private void removeOldLogs() {
        File[] files = logsDir.listFiles(file -> {
            String fileName = file.getName();
            return fileName.matches(Pattern.quote(logName) + "-\\d{4}-\\d{2}-\\d{2}\\.log");
        });

        if (files == null) {
            throw new IllegalArgumentException();
        }

        Arrays.sort(files);

        int remainingExtras = Math.max(files.length - historyLimit, 0);

        for (File file : files) {
            if (remainingExtras <= 0) break;
            boolean deleted = file.delete();
            remainingExtras--;
        }
    }

    /* -------------------- */

    @Override
    public void publish(LogRecord record) {
        String message = formatter.format(record);

        Date dateNow = new Date();
        String expectedFile = outputFileByDate(dateNow);

        // Targeted file is invalid, change it
        if (!targetFile.getName().equalsIgnoreCase(expectedFile)) {
            changeTargetFile(expectedFile);
        }

        // Log message!
        targetPrintWriter.println(message);
        hooks.forEach(hook -> hook.accept(message));
    }

    @Override
    public void flush() {
        this.targetPrintWriter.flush();
    }

    @Override
    public void close() throws SecurityException {
        this.targetPrintWriter.close();
    }

}
