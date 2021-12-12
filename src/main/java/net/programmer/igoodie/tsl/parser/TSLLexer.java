package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TSLLexer {

    private final List<String> lines;
    private final List<TSLSnippetBuffer> snippets = new LinkedList<>();

    private final TSLTokenizer tokenizer = new TSLTokenizer();

    private int lineNo = 0, charNo = 0;
    private int tokenBeginLine = -1, tokenBeginChar = -1;
    private StringBuilder characterBuffer = new StringBuilder();
    private TSLSnippetBuffer snippetBuffer = new TSLSnippetBuffer();

    boolean escaping = false;
    boolean inComment = false;
    boolean inBlockComment = false;
    boolean inGroup = false;
    boolean inExpression = false;
    boolean inParameter = false;
    boolean inCallArguments = false;
    int nestLevel = 0;

    private int lineOffset;
    private int charOffset;

    public TSLLexer(String script) {
        this(Arrays.asList(script.split("\\r?\\n")));
    }

    public TSLLexer(List<String> lines) {
        this.lines = lines;
    }

    public TSLLexer withOffset(int lineOffset, int charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
        return this;
    }

    public List<TSLSnippetBuffer> getSnippets() {
        return snippets;
    }

    protected boolean inNest() {
        return nestLevel >= 1;
    }

    protected boolean allowedParameterCharacter(char character) {
        // Allows: [a-zA-Z_]
        return Character.isLetter(character)
                || character == '_';
    }

    public TSLLexer lex() {
        for (lineNo = 0; lineNo < lines.size(); lineNo++) {
            String line = lines.get(lineNo);
            List<TSLToken> tokens = snippetBuffer.getTokens();

            if (escaping) {
                throw new TSLSyntaxError("Invalid escape sequence", lineNo(), charNo());
            }

            if (tokens.size() != 0 && TSLSymbol.equals(tokens.get(0), TSLSymbol.Type.RULESET_TAG_BEGIN)) {
                pushSnippet();
            }

            if (!inBlockComment && line.trim().isEmpty()) { // An empty line between snippets
                pushSnippet();
                continue;
            }

            if (nestLevel == 0) {
                pushToken();
            }

            lexLine(line);
        }

        pushSnippet();

        return this;
    }

    private void lexLine(String line) {
        char[] chars = line.toCharArray();

        for (charNo = 0; charNo < chars.length; charNo++) {
            char previousCharacter = charNo == 0 ? 0 : chars[charNo - 1];
            char character = chars[charNo];
            char nextCharacter = charNo == chars.length - 1 ? 0 : chars[charNo + 1];

            if (character == '#' && nextCharacter == '!') { // #!
                if (accumulatedString().length() != 0) {
                    throw new TSLSyntaxError("Unexpected ruleset tag start", lineNo(), charNo());
                }
                pushCharacters("#!");
                continue;
            }

            if (character == '#' && nextCharacter == '*') { // #*
                inComment = true;
                inBlockComment = true;
                pushCharacters("#*");
                continue;
            }

            if (character == '*' && nextCharacter == '#') { // *#
                if (!inComment) {
                    throw new TSLSyntaxError("Unexpected comment end", lineNo(), charNo());
                }
                inComment = false;
                inBlockComment = false;
                pushCharacters("*#");
                pushSnippet(); // End of the block comment
                continue;
            }

            if (inComment && !inBlockComment) {
                continue;
            }

            if (character == '#') {
                if (!escaping && !inGroup && !inExpression) {
                    return;
                }
            }

            if (character == '\\') {
                if (nextCharacter == '\\') {
                    pushCharacters("\\\\");
                    continue;
                }
                escaping = true;
                continue;
            }

            if (character == '%') {
                if (inGroup && escaping) {
                    pushCharacter('\\');
                    pushCharacter('%');
                    escaping = false;
                    continue;
                }

                if (!escaping && !inExpression) {
                    pushCharacter('%');
                    inGroup = !inGroup;
                    continue;
                }
            }

            if (character == '{' && nextCharacter == '{') { // {{
                if (inParameter) {
                    throw new TSLSyntaxError("Unexpected parameter start", lineNo(), charNo());
                }
                inParameter = true;
                pushCharacters("{{");
                continue;
            }

            if (character == '}' && nextCharacter == '}') { // }}
                if (!inParameter) {
                    // Might have an oversight here :thinking:
                    throw new TSLSyntaxError("Unexpected parameter end", lineNo(), charNo());
                }
                inParameter = false;
                pushCharacters("}}");
                continue;
            }

            if (inParameter) {
                if (!allowedParameterCharacter(character)) {
                    throw new TSLSyntaxError("Disallowed parameter character", lineNo(), charNo());
                }
            }

            if (character == '$' && nextCharacter == '{') { // ${
                if (escaping) {
                    if (inGroup) pushCharacter('\\');
                    pushCharacters("${");
                    continue;
                }

                if (inExpression) {
                    throw new TSLSyntaxError("Unexpected expression start", lineNo(), charNo());
                }

                inExpression = true;
                pushCharacters("${");
                continue;
            }

            if (character == '}') {
                if (inExpression) {
                    inExpression = false;
                    pushCharacter('}');
                    continue;
                }
            }

            if (character == '(' && accumulatedString().startsWith("$") && !inExpression) { // $call()
                if (inCallArguments) {
                    throw new TSLSyntaxError("Unexpected character", lineNo(), charNo());
                }
                inCallArguments = true;
                pushCharacter('(');
                continue;
            }

            if (character == '(' && previousCharacter == ' ' && !inExpression && !inGroup) { // Nest ((()))
                pushCharacter('(');
                nestLevel++;
                continue;
            }

            if (character == ')') {
                if (!inGroup && !inExpression) {
                    if (inNest()) {
                        pushCharacter(')');
                        nestLevel--;
                        continue;
                    }
                    if (!inCallArguments) {
                        throw new TSLSyntaxError("Unexpected character", lineNo(), charNo());
                    }
                    inCallArguments = false;
                    pushCharacter(')');
                    continue;
                }
            }

            if (character == ' ') {
                if (escaping) {
                    throw new TSLSyntaxError("Invalid escape sequence", lineNo(), charNo() - 1);
                }

                if (!inGroup && !inExpression && !inCallArguments && !inNest()) {
                    pushToken();
                    continue;
                }
            }

            escaping = false;
            pushCharacter(character);
        }
    }

    /* ---------------------------------------- */

    private int lineNo() {
        return lineNo + lineOffset;
    }

    private int charNo() {
        return charNo + charOffset;
    }

    private String accumulatedString() {
        return characterBuffer.toString().trim();
    }

    private int accumulatedCharacterLength() {
        return characterBuffer.length();
    }

    /* ---------------------------------------- */

    private void skipCharacters(int n) {
        charNo += n;
    }

    private void pushCharacter(char character) {
        this.characterBuffer.append(character);
        if (tokenBeginLine == -1) tokenBeginLine = lineNo;
        if (tokenBeginChar == -1) tokenBeginChar = charNo;
    }

    private void pushCharacters(String characters) {
        for (char character : characters.toCharArray()) {
            pushCharacter(character);
        }
        skipCharacters(characters.length() - 1);
    }

    private void pushToken() {
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

    private void pushSnippet() {
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

}
