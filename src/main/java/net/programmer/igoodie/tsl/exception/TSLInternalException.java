package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.util.LogFormatter;

public class TSLInternalException extends TSLRuntimeException {

    public TSLInternalException(String format, Object... args) {
        super(args.length == 0 ? format : LogFormatter.format(format, args));
    }

}
