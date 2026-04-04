package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;

public class TSLCaptureCall extends TSLToken {

    protected final TSLCaptureId id;
    protected final List<TSLToken> args;

    public TSLCaptureCall(TSLCaptureId id, List<TSLToken> args) {
        this.id = id;
        this.args = args;
    }

    public TSLCaptureId getId() {
        return id;
    }

    public List<TSLToken> getArgs() {
        return args;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("CaptureCalls aren't meant for evaluation.");
    }

}
