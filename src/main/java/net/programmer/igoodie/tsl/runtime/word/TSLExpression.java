package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLExpression extends TSLWord {

    protected final String script;

    public TSLExpression(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("CaptureIds aren't meant for evaluation.");
    }

}
