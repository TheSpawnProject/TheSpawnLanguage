package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

public class TSLRuntimeError extends RuntimeException {

    protected String filePath;
    protected int line, character;

    public TSLRuntimeError(String reason, TSLSnippetBuffer snippet) {
        this(reason, snippet.getTokens().get(0));
    }

    public TSLRuntimeError(String reason, TSLToken token) {
        this(reason, null, token.getLine(), token.getCharacter());
    }

    public TSLRuntimeError(String reason, int line, int character) {
        this(reason, null, line, character);
    }

    public TSLRuntimeError(String reason, String filePath, int line, int character) {
        super(reason);
        this.line = line;
        this.character = character;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return String.format("Runtime Error: %s caused by (line:%d, char:%d) %s",
                getMessage(), line, character, filePath == null ? "" : filePath);
    }

}
