package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.legacy.runtime.TSLRuleOld;
import org.jetbrains.annotations.Nullable;

public class TSLInternalError extends TSLException {

    @Override
    public String headerString() {
        return "Internal Error";
    }

    public TSLInternalError(String reason) {
        super(reason);
    }

    public TSLInternalError(String reason, TSLRuleOld rule) {
        super(reason, rule);
    }

    public TSLInternalError(String reason, TSLSnippet snippet) {
        super(reason, snippet);
    }

    public TSLInternalError(String reason, TSLTokenBuffer tokenBuffer) {
        super(reason, tokenBuffer);
    }

    public TSLInternalError(String reason, TSLToken token) {
        super(reason, token);
    }

    public TSLInternalError(String reason, int line, int character) {
        super(reason, line, character);
    }

    public TSLInternalError(String reason, @Nullable String filePath, int line, int character) {
        super(reason, filePath, line, character);
    }

}
