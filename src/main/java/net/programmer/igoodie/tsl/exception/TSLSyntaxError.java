package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.jetbrains.annotations.Nullable;

public class TSLSyntaxError extends TSLException {

    @Override
    public String headerString() {
        return "Syntax Error";
    }

    public TSLSyntaxError(String reason) {
        super(reason);
    }

    public TSLSyntaxError(String reason, TSLRule rule) {
        super(reason, rule);
    }

    public TSLSyntaxError(String reason, TSLSnippet snippet) {
        super(reason, snippet);
    }

    public TSLSyntaxError(String reason, TSLTokenBuffer tokenBuffer) {
        super(reason, tokenBuffer);
    }

    public TSLSyntaxError(String reason, TSLToken token) {
        super(reason, token);
    }

    public TSLSyntaxError(String reason, int line, int character) {
        super(reason, line, character);
    }

    public TSLSyntaxError(String reason, @Nullable String filePath, int line, int character) {
        super(reason, filePath, line, character);
    }

}
