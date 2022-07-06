package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLLexer {

    private final List<String> lines;
    private final TSLTokenizer tokenizer;
    private final List<TSLTokenBuffer> snippets;

    private int lineOffset;
    private int charOffset;

    private boolean usingCommaDelimiter;
    private boolean parsingExtraSpaces;
    private LexerMode mode = new LexerModePlainWord(this);
    private int lineNo = 0, charNo = 0;
    private String line;
    private char[] lineChars;
    private int tokenBeginLine = -1, tokenBeginChar = -1;
    private StringBuilder characterBuffer = new StringBuilder();
    private TSLTokenBuffer tokenBuffer = new TSLTokenBuffer();

    public TSLLexer(String script) {
        this(Arrays.asList(script.split("\\r?\\n")));
    }

    public TSLLexer(List<String> lines) {
        this.lines = lines;
        this.snippets = new LinkedList<>();
        this.tokenizer = new TSLTokenizer();
    }

    public TSLLexer useCommaDelimiter() {
        this.usingCommaDelimiter = true;
        return this;
    }

    public TSLLexer parseExtraSpaces() {
        this.parsingExtraSpaces = true;
        return this;
    }

    public TSLLexer withOffset(int lineOffset, int charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
        return this;
    }

    /* ------------------- */

    public TSLLexer lex() {
        lineLoop:
        for (lineNo = 0; lineNo < lines.size(); lineNo++) {
            line = lines.get(lineNo);
            lineChars = line.toCharArray();
            List<TSLToken> tokens = tokenBuffer.getTokens();

            if (tokens.size() != 0 && TSLSymbol.equals(tokens.get(0), TSLSymbol.Type.RULESET_TAG_BEGIN)) {
                pushSnippet();
            }

            if (!(mode instanceof LexerModeBlockComment) && line.trim().isEmpty()) { // An empty line between snippets
                pushSnippet();
                continue;
            }

            if (!(mode instanceof LexerModeNest)) { // Not in a nest, and it's a new line break
                pushToken();
            }

            for (charNo = 0; charNo < lineChars.length; charNo++) {
                char character = lineChars[this.charNo];
                LexResult result = mode.step(lineNo(), charNo(), character);
                if (result.shouldPushToken()) pushToken();
                if (result.getChangeMode() != null) mode = result.getChangeMode();
                if (result.shouldSkipLine()) continue lineLoop;
            }

            if (accumulatedString().endsWith("\\")) {
                throw new TSLSyntaxError("Invalid escape sequence", lineNo(), charNo());
            }
        }

        pushSnippet();

        return this;
    }

    /* ---------------------------------------- */

    private int lineNo() {
        return lineNo + lineOffset;
    }

    private int charNo() {
        return charNo + charOffset;
    }

    public boolean isUsingCommaDelimiter() {
        return usingCommaDelimiter;
    }

    public boolean isParsingExtraSpaces() {
        return parsingExtraSpaces;
    }

    private boolean inCharacterRange(int index) {
        if (index < 0) return false;
        return index < this.lineChars.length;
    }

    protected char getCharacter(int offset) {
        int charIndex = charNo + offset;
        return inCharacterRange(charIndex) ? lineChars[charIndex] : 0;
    }

    protected String accumulatedString() {
        return characterBuffer.toString().trim();
    }

    protected int accumulatedCharacterLength() {
        return characterBuffer.length();
    }

    protected TSLTokenBuffer getTokenBuffer() {
        return tokenBuffer;
    }

    public List<TSLTokenBuffer> getSnippets() {
        return snippets;
    }

    /* ---------------------------------------- */

    protected void skipCharacters(int n) {
        charNo += n;
    }

    protected void pushCharacter(char character) {
        this.characterBuffer.append(character);
        if (tokenBeginLine == -1) tokenBeginLine = lineNo;
        if (tokenBeginChar == -1) tokenBeginChar = charNo;
    }

    protected void pushCharacters(String characters) {
        for (char character : characters.toCharArray()) {
            pushCharacter(character);
        }
        skipCharacters(characters.length() - 1);
    }

    protected void pushToken() {
        if (characterBuffer.length() != 0) {
            String text = characterBuffer.toString();

            TSLToken token = tokenizer.tokenize(text,
                    1 + tokenBeginLine + lineOffset,
                    1 + tokenBeginChar + charOffset);

            if (tokenBuffer.getTokens().size() == 0) { // Inserting the very first token
                if (TSLSymbol.equals(token, TSLSymbol.Type.RULESET_TAG_BEGIN)) {
                    tokenBuffer.setType(TSLTokenBuffer.Type.TAG);
                } else if (TSLSymbol.equals(token, TSLSymbol.Type.MULTI_LINE_COMMENT_BEGIN)) {
                    tokenBuffer.setType(TSLTokenBuffer.Type.COMMENT);
                } else if (TSLSymbol.equals(token, TSLSymbol.Type.TSLDOC_BEGIN)) {
                    tokenBuffer.setType(TSLTokenBuffer.Type.TSLDOC);
                }
            }

            if (tokenBuffer.getTokens().size() == 1) { // Inserting the second token
                TSLToken firstToken = tokenBuffer.getTokens().get(0);

                if (firstToken instanceof TSLCaptureCall) {
                    if (TSLSymbol.equals(token, TSLSymbol.Type.CAPTURE_DECLARATION)) {
                        tokenBuffer.setType(TSLTokenBuffer.Type.CAPTURE);
                    }
                }
            }

            if (tokenBuffer.getTokens().size() == 3) { // Inserting the fourth token
                TSLToken firstToken = tokenBuffer.getTokens().get(0);
                TSLToken secondToken = tokenBuffer.getTokens().get(1);
                TSLToken thirdToken = tokenBuffer.getTokens().get(2);

                if (firstToken instanceof TSLDecoratorCall
                        && secondToken instanceof TSLCaptureCall
                        && thirdToken instanceof TSLSymbol
                        && ((TSLCaptureCall) secondToken).getArgs()
                        .stream().allMatch(arg -> arg instanceof TSLPlainWord)
                        && ((TSLSymbol) thirdToken).getType() == TSLSymbol.Type.CAPTURE_DECLARATION) {
                    throw new TSLSyntaxError("Captures CANNOT be decorated.", firstToken);
                }
            }

            tokenBuffer.pushToken(token);
            characterBuffer = new StringBuilder();
        }

        tokenBeginLine = -1;
        tokenBeginChar = -1;
    }

    protected void pushSnippet() {
        if (characterBuffer.length() != 0) {
            pushToken();
        }

        if (tokenBuffer.getType() == null) {
            tokenBuffer.setType(TSLTokenBuffer.Type.RULE);
        }

        if (tokenBuffer.getTokens().size() != 0) {
            snippets.add(tokenBuffer);
        }

        tokenBuffer = new TSLTokenBuffer();
    }

    /* ---------------------------------------- */

    public static List<TSLToken> lexIntoTokens(TSLLexer lexer) {
        lexer.lex();
        List<TSLTokenBuffer> snippets = lexer.getSnippets();

        if (snippets.isEmpty()) return Collections.emptyList();

        TSLTokenBuffer buffer = snippets.get(0);
        return new LinkedList<>(buffer.getTokens());
    }

    public static List<TSLToken> lexGroupTokens(String text) {
        return lexIntoTokens(new TSLLexer(text).parseExtraSpaces());
    }

    public static List<String> lexGroupRaw(String text) {
        return lexGroupTokens(text).stream()
                .map(TSLToken::getRaw)
                .collect(Collectors.toList());
    }

    public static List<TSLToken> lexArgumentTokens(String text) {
        return lexIntoTokens(new TSLLexer(text).useCommaDelimiter());
    }

    public static List<String> lexArgumentsRaw(String text) {
        return lexArgumentTokens(text).stream()
                .map(TSLToken::getRaw)
                .collect(Collectors.toList());
    }

}
