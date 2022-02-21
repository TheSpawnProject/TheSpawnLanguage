package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.jetbrains.annotations.Nullable;

public class TSLSyntaxError extends RuntimeException {

    protected String filePath;
    protected int line, character;
    protected @Nullable TSLToken associatedToken;

    public TSLSyntaxError(String reason, TSLSnippet snippet) {
        this(reason, snippet.getAllTokens().get(0));
    }

    public TSLSyntaxError(String reason, TSLSnippetBuffer snippetBuffer) {
        this(reason, snippetBuffer.getTokens().get(0));
    }

    public TSLSyntaxError(String reason, TSLRule rule) {
        this(reason, rule.getSnippet().getAllTokens().get(0));
    }

    public TSLSyntaxError(String reason, TSLToken token) {
        this(reason, null, token.getLine(), token.getCharacter());
        associatedToken = token;
    }

    public TSLSyntaxError(String reason, int line, int character) {
        this(reason, null, line + 1, character + 1);
    }

    public TSLSyntaxError(String reason, String filePath, int line, int character) {
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
        return String.format("Syntax Error: %s @ (line:%d, char:%d) %s",
                getMessage(), line, character, filePath == null ? "" : filePath);
    }

}
