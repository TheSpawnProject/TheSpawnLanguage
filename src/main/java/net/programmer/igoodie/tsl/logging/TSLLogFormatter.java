package net.programmer.igoodie.tsl.logging;

import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TSLLogFormatter extends Formatter {

    private final String name;

    public TSLLogFormatter(String name) {
        this.name = name;
    }

    @Override
    public String format(LogRecord record) {
        String level = record.getLevel().getName();
        long unixSeconds = Instant.now().getEpochSecond();
        String message = record.getMessage();

        return String.format("[%d] [%s] [%s] %s", unixSeconds, this.name, level, message);
    }

}
