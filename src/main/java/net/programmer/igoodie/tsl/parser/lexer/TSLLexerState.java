package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.lexer.mode.LexerMode;
import net.programmer.igoodie.tsl.parser.lexer.mode.LexerModeRoot;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;

import java.util.Stack;
import java.util.function.Function;

public class TSLLexerState {

    private TSLLexer lexer;

    protected String[] lines;
    protected int scanningLine = 0;
    protected int scanningColumn = 0;

    protected boolean allowsCommaDelimiter = false;

    protected int beginLineNo = 0;
    protected int beginColumnNo = 0;
    protected int endLineNo = 0;
    protected int endColumnNo = 0;
    protected boolean begunWhileEscaping = false;
    protected StringBuilder charBuffer = new StringBuilder();

    protected Stack<TSLUnparsedSnippet> snippetStack = new Stack<>();

    protected Stack<LexerMode> modeStack = new Stack<>();

    public TSLLexerState(TSLLexer lexer, String[] lines) {
        this.lexer = lexer;
        this.lines = lines;
        pushSnippet();
        pushMode(new LexerModeRoot());
    }

    private TSLLexerState(TSLLexerState stateToCopy) {
        this(stateToCopy.lexer, stateToCopy.lines);
        this.scanningLine = stateToCopy.scanningLine;
        this.scanningColumn = stateToCopy.scanningColumn;
    }

    public Couple<TSLLexer, TSLLexerState> copyLexerSet() {
        TSLLexerState state = new TSLLexerState(this);
        TSLLexer lexer = new TSLLexer(state);
        state.lexer = lexer;
        return new Couple<>(lexer, state);
    }

    public TSLLexerState withCommaDelimiterAllowed() {
        this.allowsCommaDelimiter = true;
        return this;
    }

    public boolean doesAllowCommaDelimiter() {
        return allowsCommaDelimiter;
    }

    public int getScanningLine() {
        return scanningLine;
    }

    public int getScanningColumn() {
        return scanningColumn;
    }

    public StringBuilder getAccumulatedToken() {
        return charBuffer;
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

    public int getModeDepth() {
        return this.modeStack.size();
    }

    /* ---------------------- */

    public void markBegunWhileEscaping() {
        this.begunWhileEscaping = true;
    }

    public void skipCharacters(String text) {
        this.scanningColumn += text.length();
        this.endColumnNo += text.length();
    }

    public void moveScanningPosTo(TSLLexerState otherState) {
        System.out.println("Moving pos to @ " + otherState.scanningLine + " " + otherState.scanningColumn);
        this.scanningLine = otherState.scanningLine;
        this.scanningColumn = otherState.scanningColumn;
        this.endLineNo = otherState.endLineNo;
        this.endColumnNo = otherState.endColumnNo;
    }

    protected String buildTokenRaw() {
        String raw = charBuffer.toString();
        charBuffer.setLength(0);
        return raw;
    }

    public TextRange constructTextRange() {
        return new TextRange(beginLineNo, beginColumnNo, endLineNo, endColumnNo);
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

    public int getNestDepth() {
        return snippetStack.size();
    }

    public void pushChars(String chars) {
        pushChars(chars.toCharArray());
    }

    public void pushChars(char... characters) {
        if (characters.length == 1 && characters[0] == 0) {
            System.out.println(1);
        }
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
        TextRange range = constructTextRange();
        TSLToken token = TSLTokenizer.tokenizeStateAware(range, raw, this);
        getCurrentSnippet().pushToken(token);
        begunWhileEscaping = false;
    }

    public void pushToken(Function<String, TSLToken> tokenGenerator) {
        getCurrentSnippet().pushToken(tokenGenerator.apply(buildTokenRaw()));
        begunWhileEscaping = false;
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
