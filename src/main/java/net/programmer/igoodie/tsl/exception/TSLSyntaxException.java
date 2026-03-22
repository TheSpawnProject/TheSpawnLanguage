package net.programmer.igoodie.tsl.exception;

import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.LogFormatter;
import org.antlr.v4.runtime.Token;

public class TSLSyntaxException extends TSLException {

    protected int lineNo = -1;
    protected int charNo = -1;

    public TSLSyntaxException(String format, Object... args) {
        super(args.length == 0 ? format : LogFormatter.format(format, args));
    }

    public int getLineNo() {
        return lineNo;
    }

    public int getCharNo() {
        return charNo;
    }

    public TSLSyntaxException atPos(int lineNo, int charNo) {
        this.lineNo = lineNo;
        this.charNo = charNo;
        return this;
    }

    public TSLSyntaxException atWord(TSLWord word) {
        Token token = word.getSource().get(0);
        return this.atPos(token.getLine(), token.getCharPositionInLine());
    }

    @Override
    public String getMessage() {
        if (this.lineNo != -1) {
            StringBuilder pos = new StringBuilder(" (");
            if (this.lineNo != -1) pos.append("Ln ").append(this.lineNo);
            if (this.charNo != -1) pos.append(", Col ").append(this.charNo);
            return super.getMessage() + pos.append(")");
        }

        return super.getMessage();
    }
}
