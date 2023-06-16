package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.exception.base.TSLException;

public class TSLInternalError extends TSLException {

    public TSLInternalError(String message, Object... args) {
        super(message, args);
    }

    @Override
    public String headerString() {
        return "Internal Error";
    }

}
