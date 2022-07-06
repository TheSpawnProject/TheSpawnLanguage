package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.legacy.runtime.TSLRuleOld;
import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.jetbrains.annotations.Nullable;

public class TSLRuntimeError extends TSLException {

    @Override
    public String headerString() {
        return "Runtime Error";
    }

    public TSLRuntimeError(String reason) {
        super(reason);
    }

    public TSLRuntimeError(String reason, TSLRuleOld rule) {
        super(reason, rule);
    }

    public TSLRuntimeError(String reason, TSLSnippet snippet) {
        super(reason, snippet);
    }

    public TSLRuntimeError(String reason, TSLTokenBuffer tokenBuffer) {
        super(reason, tokenBuffer);
    }

    public TSLRuntimeError(String reason, TSLToken token) {
        super(reason, token);
    }

    public TSLRuntimeError(String reason, int line, int character) {
        super(reason, line, character);
    }

    public TSLRuntimeError(String reason, @Nullable String filePath, int line, int character) {
        super(reason, filePath, line, character);
    }

}
