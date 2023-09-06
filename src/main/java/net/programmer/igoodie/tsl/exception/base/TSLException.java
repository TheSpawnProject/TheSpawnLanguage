package net.programmer.igoodie.tsl.exception.base;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class TSLException extends RuntimeException {

    protected @Nullable String filePath;
    protected @Nullable TextPosition beginningPos;
    protected @Nullable TextPosition endingPos;

    public TSLException(String message, Object... args) {
        super(MessageFormatter.format(message, args));
    }

    public TSLException causedBy(Throwable e) {
        this.initCause(e);
        return this;
    }

    public TSLException in(File file) {
        return in(file.getAbsolutePath());
    }

    public TSLException in(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public TSLException at(TSLSnippet<?> snippet) {
        this.beginningPos = snippet.getBeginningPos();
        this.endingPos = snippet.getEndingPos();
        return this;
    }

    public TSLException at(TSLToken token) {
        this.beginningPos = token.getBeginningPos();
        this.endingPos = token.getEndingPos();
        return this;
    }

    public TSLException at(TextRange range) {
        this.beginningPos = range.getBeginPos();
        this.endingPos = range.getEndPos();
        return this;
    }

    public TSLException at(int line, int col) {
        this.beginningPos = new TextPosition(line, col);
        this.endingPos = new TextPosition(line, col);
        return this;
    }

    public abstract String headerString();

    public String refString() {
        List<String> refs = new LinkedList<>();

        refs.add(getMessage());

        if (this.beginningPos != null) {
            refs.add(String.format("@ (line:%d, char:%d)",
                    this.beginningPos.getLine() + 1,
                    this.beginningPos.getCol() + 1));
        }

        if (this.filePath != null) {
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

    @Deprecated
    public TSLException at(net.programmer.igoodie.legacy.parser.token.TSLToken legacyToken) {
        return this;
    }

    @Deprecated
    public TSLException at(net.programmer.igoodie.legacy.parser.snippet.TSLSnippet legacySnippet) {
        return this;
    }

}
