package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.token.TSLToken;

public class TSLSyntaxError extends RuntimeException {

    protected String filePath;
    protected int line, character;

    public TSLSyntaxError(String reason, TSLToken token) {
        this(reason, null, token.getLine(), token.getCharacter());
    }

    public TSLSyntaxError(String reason, String filePath, int line, int character) {
        super(reason);
        this.line = line;
        this.character = character;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return String.format("Syntax Error: %s @ (l:%d, c:%d) %s",
                getCause(), line, character, filePath);
    }

}
