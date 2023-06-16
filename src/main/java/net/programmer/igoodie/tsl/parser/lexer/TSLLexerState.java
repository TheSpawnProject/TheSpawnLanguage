package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.lexer.mode.LexerMode;
import net.programmer.igoodie.tsl.parser.lexer.mode.LexerModeRoot;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

import java.util.Stack;

public class TSLLexerState {

    private final TSLLexer lexer;

    protected String[] lines;
    protected int scanningLine = 0;
    protected int scanningColumn = 0;

    protected int beginLineNo = 0;
    protected int beginColumnNo = 0;
    protected int endLineNo = 0;
    protected int endColumnNo = 0;
    protected StringBuilder charBuffer = new StringBuilder();

    protected Stack<TSLUnparsedSnippet> snippetStack = new Stack<>();

    protected Stack<LexerMode> modeStack = new Stack<>();

    public TSLLexerState(TSLLexer lexer, String[] lines) {
        this.lexer = lexer;
        this.lines = lines;
        pushSnippet();
        pushMode(new LexerModeRoot());
    }

    public TSLUnparsedSnippet getCurrentSnippet() {
        return snippetStack.peek();
    }

    public String getCurrentLine() {
        return this.lines[this.scanningLine];
    }

    public char getCharacterByOffset(int offset) {
        int col = this.scanningColumn + offset;
        String line = getCurrentLine();
        if (col < 0 || col >= line.length()) return 0;
        return line.charAt(col);
    }

    /* ---------------------- */

    protected String buildTokenRaw() {
        String raw = charBuffer.toString();
        charBuffer.setLength(0);
        return raw;
    }

    public void finalizeSnippet() {
        if (snippetStack.size() != 1) {
            throw new TSLSyntaxError("')' expected")
                    .at(scanningLine, scanningColumn);
        }

        this.lexer.snippets.add(popSnippet());
        pushSnippet();
    }

    /* ---------------------- */

    public void pushChar(char... characters) {
        if (this.charBuffer.length() == 0) {
            this.beginLineNo = this.scanningLine;
            this.beginColumnNo = this.scanningColumn;
        }

        this.charBuffer.append(characters);

        if (characters.length >= 2) {
            this.scanningColumn += characters.length - 1;
        }

        this.endLineNo = this.scanningLine;
        this.endColumnNo = this.scanningColumn;
    }

    public void pushToken() {
        String raw = buildTokenRaw();
        if (raw.length() == 0) return;
        TextRange range = new TextRange(beginLineNo, beginColumnNo, endLineNo, endColumnNo);
        TSLToken token = TSLTokenizer.tokenize(range, raw);
        getCurrentSnippet().pushToken(token);
    }

    public void pushSnippet() {
        TSLUnparsedSnippet newSnippet = new TSLUnparsedSnippet();

        if (snippetStack.isEmpty()) {
            snippetStack.push(newSnippet);
        } else {
            getCurrentSnippet().pushSnippet(newSnippet);
            snippetStack.push(newSnippet);
        }
    }

    public TSLUnparsedSnippet popSnippet() {
        return this.snippetStack.pop();
    }

    public void pushMode(LexerMode mode) {
        this.modeStack.push(mode);
    }

    public void popMode() {
        this.modeStack.pop();
    }

}
