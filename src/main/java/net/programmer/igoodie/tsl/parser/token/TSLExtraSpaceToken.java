package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;

// Intended for internal usage in TSLGroup tokens
public class TSLExtraSpaceToken extends TSLToken {

    private int amount;

    public TSLExtraSpaceToken(int line, int character, int amount) {
        super(line, character);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String getTypeName() {
        return "Extra Space";
    }

    @Override
    public String getRaw() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.append(' ');
        }
        return builder.toString();
    }

    @Override
    public String evaluate(TSLContext context) {
        return getRaw();
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", getClass().getSimpleName(), amount);
    }

}
