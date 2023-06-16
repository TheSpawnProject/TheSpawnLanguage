package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.exception.base.TSLException;

public class TSLSyntaxError extends TSLException {

    public TSLSyntaxError(String message, Object... args) {
        super(message, args);
    }

    @Override
    public String headerString() {
        return "Syntax Error";
    }

}
