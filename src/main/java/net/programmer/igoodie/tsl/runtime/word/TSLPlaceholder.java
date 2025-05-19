package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLPlaceholder extends TSLWord {

    protected final String parameterName;

    public TSLPlaceholder(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        throw new IllegalStateException("Placeholders aren't meant for evaluation.");
    }

}
