package net.programmer.igoodie.tsl.logging;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class TSLLogLevel extends Level {

    public static final List<TSLLogLevel> LOGGABLE_LEVELS = new LinkedList<>();

    public static final TSLLogLevel OFF = new TSLLogLevel("OFF", Integer.MAX_VALUE);

    public static final TSLLogLevel FATAL = registerLoggable("FATAL", 600);
    public static final TSLLogLevel ERROR = registerLoggable("ERROR", 500);
    public static final TSLLogLevel WARN = registerLoggable("WARN", 400);
    public static final TSLLogLevel INFO = registerLoggable("INFO", 300);
    public static final TSLLogLevel DEBUG = registerLoggable("DEBUG", 200);
    public static final TSLLogLevel TRACE = registerLoggable("TRACE", 100);

    public static final TSLLogLevel ALL = new TSLLogLevel("ALL", Integer.MIN_VALUE);

    /* -------------------------- */

    private static TSLLogLevel registerLoggable(String name, int value) {
        TSLLogLevel level = new TSLLogLevel(name, value);
        LOGGABLE_LEVELS.add(level);
        return level;
    }

    /* -------------------------- */

    private TSLLogLevel(String name, int value) {
        super(name, value, "net.programmer.igoodie.tsl");
    }

}
