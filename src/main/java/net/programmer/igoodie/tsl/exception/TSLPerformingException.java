package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.util.LogFormatter;

public class TSLPerformingException extends TSLRuntimeException {

    public TSLPerformingException(String format, Object... args) {
        super(args.length == 0 ? format : LogFormatter.format(format, args));
    }

}
