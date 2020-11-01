package net.programmer.igoodie.tsl.logging;

import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TSLLogFormatter extends Formatter {

    private String name;

    public TSLLogFormatter(String name) {
        this.name = name;
    }

    @Override
    public String format(LogRecord record) {
        return String.format("[%s] [%d] [%s] %s",
                record.getLevel(),
                Instant.now().getEpochSecond(),
                this.name,
                record.getMessage());
    }

}
