package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;
import org.jetbrains.annotations.Nullable;

public class TSLPlainWord extends TSLToken {

    protected String word;

    public TSLPlainWord(int line, int character, String word) {
        super(line, character);
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public boolean isTrue() {
        String word = getWord();
        return word.equalsIgnoreCase("TRUE")
                || word.equals("1");
    }

    public boolean isFalse() {
        String word = getWord();
        return word.equalsIgnoreCase("FALSE")
                || word.equals("0");
    }

    @Nullable
    public String getNamespace() {
        if (!word.contains(".")) return null;
        return word.split("\\.")[0];
    }

    public String getValue() {
        if (!word.contains(".")) return word;
        return word.split("\\.")[1];
    }

    @Override
    public String getTypeName() {
        return "Plain Word";
    }

    @Override
    public String getRaw() {
        return getWord();
    }

    @Override
    public String evaluate(TSLContext context) {
        return getWord();
    }

}
