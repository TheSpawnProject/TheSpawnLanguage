package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

public abstract class TSLToken {

    protected int line, character;

    public TSLToken(int line, int character) {
        this.line = line;
        this.character = character;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    public abstract String getTypeName();

    public abstract String getRaw();

    public abstract String evaluate(TSLContext context);

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getRaw());
    }

}
