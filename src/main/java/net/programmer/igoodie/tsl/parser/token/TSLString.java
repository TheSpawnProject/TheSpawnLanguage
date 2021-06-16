package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

public class TSLString extends TSLToken {

    protected String word;

    public TSLString(int line, int character, String word) {
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

    @Override
    public String getTypeName() {
        return "String Word";
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
