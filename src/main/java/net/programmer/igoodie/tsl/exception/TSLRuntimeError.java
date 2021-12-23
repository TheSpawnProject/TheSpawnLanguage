package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.jetbrains.annotations.Nullable;

public class TSLRuntimeError extends RuntimeException {

    protected String filePath;
    protected int line, character;
    protected @Nullable TSLToken associatedToken;

    public TSLRuntimeError(String reason, TSLSnippetBuffer snippet) {
        this(reason, snippet.getTokens().get(0));
    }

    public TSLRuntimeError(String reason, TSLToken token) {
        this(reason, null, token.getLine(), token.getCharacter());
        associatedToken = token;
    }

    public TSLRuntimeError(String reason, int line, int character) {
        this(reason, null, line + 1, character + 1);
    }

    public TSLRuntimeError(String reason, String filePath, int line, int character) {
        super(reason);
        this.line = line;
        this.character = character;
        this.filePath = filePath;
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

    public @Nullable TSLToken getAssociatedToken() {
        return associatedToken;
    }

    @Override
    public String toString() {
        return String.format("Runtime Error: %s @ (line:%d, char:%d) %s",
                getMessage(), line, character, filePath == null ? "" : filePath);
    }

}
