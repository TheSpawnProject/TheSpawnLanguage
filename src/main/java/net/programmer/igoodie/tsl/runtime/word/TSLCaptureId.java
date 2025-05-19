package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLCaptureId extends TSLWord {

    protected final String captureName;

    public TSLCaptureId(String captureName) {
        this.captureName = captureName;
    }

    public String getCaptureName() {
        return captureName;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("CaptureIds aren't meant for evaluation.");
    }

}
