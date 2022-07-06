package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.exception.reason.ExceptionReason;
import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public abstract class TSLException extends RuntimeException {

    protected int line, character;
    protected @Nullable String filePath;
    protected @Nullable TSLToken associatedToken;

    protected List<ExceptionReason> reasons = new LinkedList<>();

    public TSLException(String reason) {
        super(reason);
    }

    public TSLException(String reason, TSLRule rule) {
        this(reason, rule.getSnippet());
    }

    public TSLException(String reason, TSLSnippet snippet) {
        this(reason, snippet.getAllTokens().get(0));
    }

    public TSLException(String reason, TSLTokenBuffer tokenBuffer) {
        this(reason, tokenBuffer.getTokens().get(0));
    }

    public TSLException(String reason, TSLToken token) {
        this(reason, null, token.getLine(), token.getCharacter());
        associatedToken = token;
    }

    public TSLException(String reason, int line, int character) {
        this(reason, null, line + 1, character + 1);
    }

    public TSLException(String reason, @Nullable String filePath, int line, int character) {
        super(reason);
        this.line = line;
        this.character = character;
        this.filePath = filePath;
    }

    // TODO: Keep implementing dis
    public TSLException withReason(ExceptionReason reason) {
        this.reasons.add(reason);
        return this;
    }

    public @Nullable String getFilePath() {
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

    public abstract String headerString();

    public String refString() {
        List<String> refs = new LinkedList<>();

        refs.add(getMessage());

        if (line > 0) {
            refs.add(String.format("@ (line:%d, char:%d)", line, character));
        }

        if (filePath != null) {
            refs.add(filePath);
        }

        return String.join(" ", refs);
    }

    @Override
    public String toString() {
        String refs = refString();
        return refs.isEmpty()
                ? headerString()
                : headerString() + ": " + refs;
    }

}
