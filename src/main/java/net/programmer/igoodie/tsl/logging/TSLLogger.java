package net.programmer.igoodie.tsl.logging;

import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class TSLLogger {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("MM-dd-yyyy--HH-mm-ss");

    private static final int MAX_LOG_TO_KEEP = 5;

    public static Logger createLogger(File parentFolder, TSLPlugin plugin, boolean useConsoleToo) {
        String pluginName = plugin.getManifest().getName().replaceAll(" ", "_");
        return createLogger(parentFolder, pluginName, useConsoleToo);
    }

    public static Logger createLogger(File logsFolder, String namespace, boolean useConsoleToo) {
        try {
            String dateString = TIME_FORMAT.format(new Date());
            String logName = namespace + "-" + dateString;

            Logger logger = Logger.getLogger(logName);
            TSLLogFormatter formatter = new TSLLogFormatter(namespace);

            File logFile = new File(logsFolder.getAbsolutePath()
                    + File.separator + namespace
                    + File.separator + logName + ".log");

            if (!logFile.exists()) {
                boolean succeeded = logFile.getParentFile().mkdirs();
            }

            removeOldLogs(logFile.getParentFile());

            logger.setUseParentHandlers(false);

            FileHandler fileHandler = new FileHandler(logFile.getAbsolutePath());
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);

            if (useConsoleToo) {
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(formatter);
                logger.addHandler(consoleHandler);
            }

            return logger;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void removeOldLogs(File parentFolder) {
        File[] files = parentFolder.listFiles();

        if (files == null)
            throw new IllegalArgumentException("");

        int length = files.length;

        int deleteCount = Math.max(length - MAX_LOG_TO_KEEP + 1, 0);

        for (File file : files) {
            if (deleteCount <= 0) break;
            boolean deleted = file.delete();
            deleteCount--;
        }
    }

}
