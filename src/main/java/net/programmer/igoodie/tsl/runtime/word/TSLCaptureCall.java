package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;

public class TSLCaptureCall extends TSLWord {

    protected final TSLCaptureId id;
    protected final List<TSLWord> args;

    public TSLCaptureCall(TSLCaptureId id, List<TSLWord> args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("CaptureCalls aren't meant for evaluation.");
    }

}
