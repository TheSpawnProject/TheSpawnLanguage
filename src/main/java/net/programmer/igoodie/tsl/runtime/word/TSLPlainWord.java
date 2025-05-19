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

    public static boolean isKeyword(TSLWord word, String expectedKeyword) {
        if (!(word instanceof TSLPlainWord)) return false;
        return ((TSLPlainWord) word).getValue().equalsIgnoreCase(expectedKeyword);
    }

}
