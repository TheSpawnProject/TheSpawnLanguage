package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.exception.base.TSLException;

public class TSLImplementationError extends TSLException {

    public TSLImplementationError(String message, Object... args) {
        super(message, args);
    }

    @Override
    public String headerString() {
        return "Implementation Error";
    }

}
