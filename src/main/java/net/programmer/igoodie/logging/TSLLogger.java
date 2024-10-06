package net.programmer.igoodie.logging;

public abstract class TSLLogger {

    public abstract void info(String msg, Object... args);

    public abstract void warn(String msg, Object... args);

    public abstract void error(String msg, Object... args);

    public abstract void debug(String msg, Object... args);

    public abstract void trace(String msg, Object... args);

}