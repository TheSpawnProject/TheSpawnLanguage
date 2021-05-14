package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

public class TSLInternalError extends InternalError {

    protected String filePath;
    protected boolean boundToScript;
    protected int line, character;

    public TSLInternalError(String reason) {
        super(reason);
    }

    public TSLInternalError(String reason, TSLSnippetBuffer snippet) {
        this(reason, snippet.getTokens().get(0));
    }

    public TSLInternalError(String reason, TSLToken token) {
        this(reason, null, token.getLine(), token.getCharacter());
    }

    public TSLInternalError(String reason, int line, int character) {
        this(reason, null, line, character);
    }

    public TSLInternalError(String reason, String filePath, int line, int character) {
        super(reason);
        this.line = line;
        this.character = character;
        this.filePath = filePath;
        this.boundToScript = true;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        if (boundToScript) {
            return String.format("Internal Error: %s caused by (line:%d, char:%d) %s",
                    getMessage(), line, character, filePath == null ? "" : filePath);
        }
        return String.format("Internal Error: %s", getMessage());
    }

}
