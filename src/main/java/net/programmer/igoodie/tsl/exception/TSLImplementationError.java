package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.jetbrains.annotations.Nullable;

public class TSLImplementationError extends TSLException {

    @Override
    public String headerString() {
        return "Implementation Error";
    }

    public TSLImplementationError(String reason) {
        super(reason);
    }

    public TSLImplementationError(String reason, TSLRule rule) {
        super(reason, rule);
    }

    public TSLImplementationError(String reason, TSLSnippet snippet) {
        super(reason, snippet);
    }

    public TSLImplementationError(String reason, TSLTokenBuffer snippet) {
        super(reason, snippet);
    }

    public TSLImplementationError(String reason, TSLToken token) {
        super(reason, token);
    }

    public TSLImplementationError(String reason, int line, int character) {
        super(reason, line, character);
    }

    public TSLImplementationError(String reason, @Nullable String filePath, int line, int character) {
        super(reason, filePath, line, character);
    }

}
