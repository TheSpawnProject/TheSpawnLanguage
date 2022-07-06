package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.jetbrains.annotations.Nullable;

public class TSLImportError extends TSLException {

    @Override
    public String headerString() {
        return "Import Error";
    }

    public TSLImportError(String reason) {
        super(reason);
    }

    public TSLImportError(String reason, TSLRule rule) {
        super(reason, rule);
    }

    public TSLImportError(String reason, TSLSnippet snippet) {
        super(reason, snippet);
    }

    public TSLImportError(String reason, TSLTokenBuffer tokenBuffer) {
        super(reason, tokenBuffer);
    }

    public TSLImportError(String reason, TSLToken token) {
        super(reason, token);
    }

    public TSLImportError(String reason, int line, int character) {
        super(reason, line, character);
    }

    public TSLImportError(String reason, @Nullable String filePath, int line, int character) {
        super(reason, filePath, line, character);
    }

}
