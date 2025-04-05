package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLPlainWord extends TSLWord {

    protected final String value;

    public TSLPlainWord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        return this.value;
    }

}
