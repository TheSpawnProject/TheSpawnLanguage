package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.exception.base.TSLException;

public class TSLImportError extends TSLException {

    public TSLImportError(String message, Object... args) {
        super(message, args);
    }

    @Override
    public String headerString() {
        return "Import Error";
    }

}
