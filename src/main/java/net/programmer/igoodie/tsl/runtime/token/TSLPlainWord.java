package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLPlainWord extends TSLToken {

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

    @Override
    public String toDebugString() {
        return this.value;
    }

    public static boolean isKeyword(TSLToken word, String expectedKeyword) {
        if (!(word instanceof TSLPlainWord)) return false;
        return ((TSLPlainWord) word).getValue().equalsIgnoreCase(expectedKeyword);
    }

}
