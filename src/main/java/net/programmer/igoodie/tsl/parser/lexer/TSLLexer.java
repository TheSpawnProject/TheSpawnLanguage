package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLLexer {

    private final List<String> lines;
    private final TSLTokenizer tokenizer;
    private final List<TSLSnippetBuffer> snippets;

    private int lineOffset;
    private int charOffset;

    private boolean usingCommaDelimiter;
    private LexerMode mode = new LexerModeString(this);
    private int lineNo = 0, charNo = 0;
    private String line;
    private char[] chars;
    private int tokenBeginLine = -1, tokenBeginChar = -1;
    private StringBuilder characterBuffer = new StringBuilder();
    private TSLSnippetBuffer snippetBuffer = new TSLSnippetBuffer();

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
            chars = line.toCharArray();
            List<TSLToken> tokens = snippetBuffer.getTokens();

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

            for (charNo = 0; charNo < chars.length; charNo++) {
                char character = chars[this.charNo];
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

    private boolean inCharacterRange(int index) {
        if (index <= 0) return false;
        return index < this.chars.length;
    }

    protected char getCharacter(int offset) {
        int charIndex = charNo + offset;
        return inCharacterRange(charIndex) ? chars[charIndex] : 0;
    }

    protected String accumulatedString() {
        return characterBuffer.toString().trim();
    }

    protected int accumulatedCharacterLength() {
        return characterBuffer.length();
    }

    public List<TSLSnippetBuffer> getSnippets() {
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

            if (snippetBuffer.getTokens().size() == 0) { // Inserting the very first token
                if (TSLSymbol.equals(token, TSLSymbol.Type.RULESET_TAG_BEGIN)) {
                    snippetBuffer.setType(TSLSnippetBuffer.Type.TAG);
                } else if (TSLSymbol.equals(token, TSLSymbol.Type.MULTI_LINE_COMMENT_BEGIN)) {
                    snippetBuffer.setType(TSLSnippetBuffer.Type.COMMENT);
                } else if (TSLSymbol.equals(token, TSLSymbol.Type.TSLDOC_BEGIN)) {
                    snippetBuffer.setType(TSLSnippetBuffer.Type.TSLDOC);
                }
            }

            if (snippetBuffer.getTokens().size() == 1) { // Inserting the second token
                TSLToken firstToken = snippetBuffer.getTokens().get(0);

                if (firstToken instanceof TSLCaptureCall) {
                    if (TSLSymbol.equals(token, TSLSymbol.Type.CAPTURE_DECLARATION)) {
                        snippetBuffer.setType(TSLSnippetBuffer.Type.CAPTURE);
                    }
                }
            }

            if (snippetBuffer.getTokens().size() == 3) { // Inserting the fourth token
                TSLToken firstToken = snippetBuffer.getTokens().get(0);
                TSLToken secondToken = snippetBuffer.getTokens().get(1);
                TSLToken thirdToken = snippetBuffer.getTokens().get(2);

                if (firstToken instanceof TSLDecoratorCall
                        && secondToken instanceof TSLCaptureCall
                        && thirdToken instanceof TSLSymbol
                        && new TSLTokenizer().tokenizeAll(((TSLCaptureCall) secondToken).getArgs())
                        .stream().allMatch(arg -> arg instanceof TSLString)
                        && ((TSLSymbol) thirdToken).getType() == TSLSymbol.Type.CAPTURE_DECLARATION) {
                    throw new TSLSyntaxError("Captures CANNOT be decorated.", firstToken);
                }
            }

            snippetBuffer.pushToken(token);
            characterBuffer = new StringBuilder();
        }

        tokenBeginLine = -1;
        tokenBeginChar = -1;
    }

    protected void pushSnippet() {
        if (characterBuffer.length() != 0) {
            pushToken();
        }

        if (snippetBuffer.getType() == null) {
            snippetBuffer.setType(TSLSnippetBuffer.Type.RULE);
        }

        if (snippetBuffer.getTokens().size() != 0) {
            snippets.add(snippetBuffer);
        }

        snippetBuffer = new TSLSnippetBuffer();
    }

    /* ---------------------------------------- */

    public static List<TSLToken> lexArgumentTokens(String text) {
        List<TSLToken> args = new LinkedList<>();
        TSLLexer lexer = new TSLLexer(text).useCommaDelimiter().lex();
        TSLSnippetBuffer snippet = lexer.getSnippets().get(0);
        if (snippet != null) {
            args.addAll(snippet.getTokens());
        }
        return args;
    }

    public static List<String> lexArgumentsRaw(String text) {
        return lexArgumentTokens(text).stream()
                .map(TSLToken::getRaw)
                .collect(Collectors.toList());
    }

}
