package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.Nullable;

public class TSLPlainWord extends TSLToken {

    protected String rawWord;

    public TSLPlainWord(int line, int character, String rawWord) {
        super(line, character);
        this.rawWord = rawWord;
    }

    public String getRawWord() {
        return rawWord;
    }

    public boolean isTrue() {
        String word = getRawWord();
        return word.equalsIgnoreCase("TRUE")
                || word.equals("1");
    }

    public boolean isFalse() {
        String word = getRawWord();
        return word.equalsIgnoreCase("FALSE")
                || word.equals("0");
    }

    @Nullable
    public String getNamespace() {
        if (!rawWord.contains(".")) return null;
        return rawWord.split("\\.")[0];
    }

    public String getValue() {
        if (!rawWord.contains(".")) return rawWord;
        return rawWord.split("\\.")[1];
    }

    @Override
    public String getTypeName() {
        return "Plain Word";
    }

    @Override
    public String getRaw() {
        return getRawWord();
    }

    @Override
    public String evaluate(TSLContext context) {
        return getRawWord()
                .replace("\\%", "%")
                .replace("\\$", "$")
                .replace("\\\\", "\\");
    }

}
