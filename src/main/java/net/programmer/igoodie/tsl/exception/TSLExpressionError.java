package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.exception.base.TSLException;

public class TSLExpressionError extends TSLException {

    public TSLExpressionError(String message, Object... args) {
        super(message, args);
    }

    @Override
    public String headerString() {
        return "Expression Error";
    }

}
