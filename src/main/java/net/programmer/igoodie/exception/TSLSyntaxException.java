package net.programmer.igoodie.exception;

import net.programmer.igoodie.util.QuickFormatter;

public class TSLSyntaxException extends TSLException {

    public TSLSyntaxException(String format, Object... args) {
        super(args.length == 0 ? format : QuickFormatter.replaceArgs(format, args));
    }

}
