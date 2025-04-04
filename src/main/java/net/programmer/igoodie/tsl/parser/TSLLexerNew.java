package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSLLexerNew {

    protected final CharStream charStream;
    protected int tokenLineNo, tokenCharNo;
    protected int cursorLineNo, cursorCharNo;

    protected List<Token> tokens = new ArrayList<>();

    public TSLLexerNew(CharStream charStream) {
        this.charStream = charStream;
    }

    protected Token pushToken(Token.Type type, String value) {
        Token token = new Token(type, this.tokenLineNo, this.tokenCharNo, value);
        this.tokenLineNo = this.cursorLineNo;
        this.tokenCharNo = this.cursorCharNo;
        return token;
    }

    public List<Token> tokenize() throws IOException, TSLSyntaxException {
        Token token;

        while (charStream.hasNext()) {
            if (charStream.peek() == ' ') {
                charStream.consume();
                this.tokenCharNo = ++this.cursorCharNo;
                continue;
            }

            if ((token = this.tokenizeEmptyLine()) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeNewLine()) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeExpression()) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeComment()) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeGroup()) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeKeyword("ON", Token.Type.KEYWORD_ON)) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeKeyword("WITH", Token.Type.KEYWORD_WITH)) != null) {
                tokens.add(token);
                continue;
            }

            if ((token = this.tokenizeWord()) != null) {
                tokens.add(token);
                continue;
            }

            throw new TSLSyntaxException("Unknown token expression")
                    .atPos(this.cursorLineNo, this.cursorCharNo);
        }

        return this.tokens;
    }

    // COMMENT = #... | #*....*#
    public Token tokenizeComment() throws IOException, TSLSyntaxException {
        StringBuilder comment = new StringBuilder();

        if (charStream.lookahead("#*")) {
            comment.append("#*");
            charStream.consume(2);
            cursorCharNo += 2;

            while (charStream.hasNext() && !charStream.lookahead("*#")) {
                comment.append(charStream.peek());
                charStream.consume();
                cursorCharNo++;
            }

            if (charStream.lookahead("*#")) {
                comment.append("*#");
                charStream.consume(2);
                return pushToken(Token.Type.COMMENT, comment.toString());
            }

            throw new TSLSyntaxException("Unfinished comment")
                    .atPos(tokenLineNo, tokenCharNo);
        }

        if (charStream.peek() == '#') {
            comment.append("#");
            charStream.consume();
            cursorCharNo++;

            while (charStream.hasNext() && !charStream.peekAhead(2).matches("\\r?\\n")) {
                comment.append(charStream.peek());
                charStream.consume();
                cursorCharNo++;
            }

            return pushToken(Token.Type.COMMENT, comment.toString());
        }

        return null;
    }

    // EMPTY_LINE = (\r?\n){2,}
    public Token tokenizeEmptyLine() throws IOException {
        for (String target : new String[]{"\r\n", "\r", "\n"}) {
            if (charStream.lookahead(target + target)) {
                StringBuilder value = new StringBuilder();

                while (charStream.lookahead(target)) {
                    value.append(target);
                    charStream.consume(target.length());
                    cursorLineNo++;
                    cursorCharNo = 0;
                }

                return pushToken(Token.Type.EMPTY_LINE, value.toString());
            }
        }

        return null;
    }

    // NEW_LINE = \r?\n
    public Token tokenizeNewLine() throws IOException {
        for (String target : new String[]{"\r\n", "\r", "\n"}) {
            if (charStream.lookahead(target)) {
                charStream.consume(target.length());
                cursorLineNo++;
                cursorCharNo = 0;
                return pushToken(Token.Type.NEW_LINE, target);
            }
        }

        return null;
    }

    // EXPRESSION = ${some * spawnjs * code}
    public Token tokenizeExpression() throws IOException, TSLSyntaxException {
        if (charStream.lookahead("${")) {
            TSLExprLexer exprLexer = new TSLExprLexer(this);
            return exprLexer.tokenizeExpression();
        }

        return null;
    }

    // GROUP = %...%
    @Deprecated(forRemoval = true)
    public Token tokenizeGroup() throws IOException, TSLSyntaxException {
        if (charStream.peek() == '%') {
            charStream.consume();
            StringBuilder sb = new StringBuilder("%");
            boolean isEscaping = false;

            while (charStream.hasNext()) {
                char curr = charStream.peek();

                if (curr == '\\') {
                    charStream.consume();

                    if (isEscaping) {
                        sb.append("\\");
                        cursorCharNo++;
                        isEscaping = false;
                    } else {
                        isEscaping = true;
                        cursorCharNo++;
                    }

                    continue;
                }

                if (curr == '%') {
                    charStream.consume();

                    if (isEscaping) {
                        sb.append("%");
                        cursorCharNo++;
                        isEscaping = false;
                    } else {
                        charStream.consume();
                        sb.append("%");
                        return pushToken(Token.Type.GROUP_MARKER, sb.toString());
                    }

                    continue;
                }

                if (isEscaping) {
                    sb.append("\\").append(curr);
                    charStream.consume();
                    cursorCharNo++;
                    isEscaping = false;
                }

                for (String newLine : new String[]{"\r\n", "\r", "\n"}) {
                    while (charStream.lookahead(newLine)) {
                        sb.append(newLine);
                        charStream.consume(newLine.length());
                        cursorLineNo++;
                        cursorCharNo = 0;
                    }
                }

                sb.append(curr);
                charStream.consume();
                cursorCharNo++;
            }

            return pushToken(Token.Type.GROUP_MARKER, sb.toString());
        }

        // GROUP_MARKER token* GROUP_MARKER
        return null;
    }

    // GROUP_MARKER %
    public Token tokenizeGroupMarker() throws IOException {
        // TODO
        return null;
    }

    // GROUP_CONTENT anything except % or ${
    public Token tokenizeGroupContent() throws IOException {
        // TODO
        return null;
    }

    // KEYWORD = ON WITH
    public Token tokenizeKeyword(String keyword, Token.Type type) throws IOException {
        String peeked = charStream.peekAhead(keyword.length());

        if (peeked.equalsIgnoreCase(keyword)) {
            charStream.consume(keyword.length());
            cursorCharNo += keyword.length();
            return pushToken(type, peeked);
        }

        return null;
    }

    // WORD = PRINT 123 FOO99
    public Token tokenizeWord() throws IOException {
        StringBuilder value = new StringBuilder();

        while (true) {
            char curr = charStream.peek();

            boolean isValid = Character.isAlphabetic(curr)
                    || Character.isDigit(curr)
                    || curr == ':'
                    || curr == '_'
                    || curr == '['
                    || curr == ']'
                    || curr == '<'
                    || curr == '>'
                    || curr == '='
                    || curr == ',';

            if (!isValid) break;

            value.append(curr);
            charStream.consume();
            cursorCharNo++;
        }

        if (value.isEmpty()) {
            return null;
        }

        return pushToken(Token.Type.WORD, value.toString());
    }

    public static void main(String[] args) throws IOException, TSLSyntaxException {
        String script = """
                # Example TSL (TwitchSpawn Language) Rule Set
                # Feel free to edit this one, or create your own rule set!
                # For full documentation about TSL Language
                # See https://igoodie.gitbook.io/twitchspawn/
                
                # Donation Rules
                DROP minecraft:stick 2
                 ON Donation
                 WITH amount IN RANGE [0,20]
                
                DROP ${"minecraft\\:diamond_block"} 1
                 ON Donation
                 WITH amount IN RANGE [21,999]
                
                EXECUTE %/gamerule keepInventory true%
                 ON Donation
                 WITH amount >= 1000
                
                # Follow Rules
                DROP %diamond{display:{Name:"\\"99.99\\% Percent Real Diamond\\""}}% 2
                 ON Twitch Follow
                
                # Host Rules
                EXECUTE %/give @s stick 1%
                 ON Twitch Host
                 WITH viewers >= 10
                
                # Raid Rules
                EXECUTE %/weather thunder%
                 ON Twitch Raid
                 WITH raiders > 100
                """;

        // Debug CRLF
        script = script.replaceAll("\\n", "\r\n");

        System.out.println(script
                .replaceAll("\\r", "\\\\r")
                .replaceAll("\\n", "\\\\n"));

        TSLLexerNew lexer = new TSLLexerNew(CharStream.fromString(script));
        List<Token> tokens = lexer.tokenize();
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            System.out.println(i + " " + token);
        }
    }

    public static final class Token {

        public final Type type;
        public final int lineNo, charNo;
        public final String value;

        public Token(Type type, int lineNo, int charNo, String value) {
            this.type = type;
            this.lineNo = lineNo;
            this.charNo = charNo;
            this.value = value;
        }

        public Type type() {
            return type;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", lineNo=" + lineNo +
                    ", charNo=" + charNo +
                    ", value='" + value
                    .replaceAll("\\r", "\\\\r")
                    .replaceAll("\\n", "\\\\n") + '\'' +
                    '}';
        }

        public enum Type {
            WORD, // PRINT
            EXPR, // ${some * spawnjs * code}
            CAPTURE, // $captureName
            GROUP_MARKER, // %
            GROUP_CONTENT, // .
            KEYWORD_ON, // ON
            KEYWORD_WITH, // WITH
            COMMENT, // # line, or #* multi-line *#
            NEW_LINE, // \r?\n
            EMPTY_LINE // (\r?\n){2,}
        }

        // Parser:
        // <ruleset> ::= (<rule> (EMPTY_LINE+ <rule>)*)? EOF
        // <rule> ::= WORD <arg>* ON WORD+ (WITH WORD+)
        // <arg> ::= anyToken | <capture>
        // <capture> ::= CAPTURE "=" anyToken+

        // TSL1.5 Features
        // - Captures
        // - SpawnJS
        // - Expressions (via SpawnJS)
        // - Custom Integrations (via SpawnJS)
        // - Custom Actions API (Java)
        // - TSL Platform-specific LSP Server

        // TSL2.0 Features Planned
        // - Capture Params
        // - Rule Decorators
        // - Ruleset Tags
        // - #! INCLUDE support
        // - Nestable Statements
        // - Upgrade from SpawnJS to NodeJS
        // - Universal TSL API (via NodeJS)
        // - TSL as an OS Service
    }

}
