package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.util.LogFormatter;

public class TSLSyntaxException extends TSLException {

    public TSLSyntaxException(String format, Object... args) {
        super(args.length == 0 ? format : LogFormatter.format(format, args));
    }

}
