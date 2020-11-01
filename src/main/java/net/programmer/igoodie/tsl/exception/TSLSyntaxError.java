package net.programmer.igoodie.tsl.exception;

public class TSLSyntaxError extends RuntimeException {

    protected String filePath;
    protected int line, character;

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
