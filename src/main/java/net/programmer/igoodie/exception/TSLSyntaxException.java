package net.programmer.igoodie.exception;

public class TSLSyntaxException extends TSLException {

    public TSLSyntaxException(String messageFormat, Object... args) {
        super(args.length == 0 ? messageFormat : String.format(messageFormat, args));
    }

}
