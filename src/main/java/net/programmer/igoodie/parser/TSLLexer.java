package net.programmer.igoodie.parser;

import net.programmer.igoodie.exception.TSLSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSLLexer {

    protected final CharStream charStream;

    protected boolean inSingleComment = false;
    protected boolean inMultiComment = false;
    protected boolean inGroup = false;
    protected boolean isEscaping = false;
    protected boolean prevNewLine = true;

    protected StringBuilder sb = new StringBuilder();

    public TSLLexer(CharStream charStream) {
        this.charStream = charStream;
    }

    public List<Token> tokenize() throws IOException, TSLSyntaxException {
        List<Token> tokens = new ArrayList<>();

        while (charStream.hasNext()) {
            char curr = charStream.peek();

            if (inSingleComment) {
                if (curr == '\n') {
                    inSingleComment = false;
                } else {
                    charStream.consume();
                    continue;
                }
            }

            if (inMultiComment) {
                if (curr == '*' && charStream.peek(2) == '#') {
                    inMultiComment = false;
                    charStream.consume(2);
                } else {
                    charStream.consume();
                }
                continue;
            }

            if (curr == '#' && charStream.peek(2) == '*') {
                inMultiComment = true;
                continue;
            }

            if (curr == '#') {
                inSingleComment = true;
                continue;
            }

            if (prevNewLine) {
                if (curr == ' ') {
                    charStream.consume();
                } else if (curr == '\n' || curr == '\r') {
                    sb.append(curr);
                    charStream.consume();
                } else {
                    if (sb.length() > 0) {
                        tokens.add(generateToken());
                        sb.setLength(0);
                    }
                    sb.append(curr);
                    charStream.consume();
                    prevNewLine = false;
                }
                continue;
            }

            if (inGroup) {
                if (isEscaping) {
                    if (curr != '%' && curr != '\\') {
                        sb.append('\\');
                    }
                    sb.append(curr);
                    charStream.consume();
                    isEscaping = false;
                    continue;
                }

                if (curr == '\\') {
                    isEscaping = true;
                    charStream.consume();
                    continue;
                }

                if (curr == '%') {
                    tokens.add(generateToken());
                    sb.setLength(0);
                    inGroup = false;
                    charStream.consume();
                    continue;
                }

                sb.append(curr);
                charStream.consume();
                continue;
            }

            if (curr == '\n' || curr == '\r') {
                if (sb.length() > 0) {
                    tokens.add(generateToken());
                    sb.setLength(0);
                }
                prevNewLine = true;
                charStream.consume();
                continue;
            }

            if (curr == '%') {
                inGroup = true;
                charStream.consume();
                continue;
            }

            if (curr == ' ') {
                if (sb.length() > 0) {
                    tokens.add(generateToken());
                    sb.setLength(0);
                }
                charStream.consume();
                continue;
            }

            sb.append(curr);
            charStream.consume();
        }

        if (inGroup) throw new TSLSyntaxException("Unclosed group");
        if (isEscaping) throw new TSLSyntaxException("Unexpected escaping");

        if (sb.length() > 0) {
            tokens.add(generateToken());
        }

        return tokens;
    }

    protected Token generateToken() {
        if (prevNewLine) return new Token(TokenType.EMPTY_LINE, sb.toString());
        if (sb.toString().equalsIgnoreCase("ON")) return new Token(TokenType.KEYWORD_ON, sb.toString());
        if (sb.toString().equalsIgnoreCase("WITH")) return new Token(TokenType.KEYWORD_WITH, sb.toString());
        return new Token(TokenType.WORD, sb.toString());
    }

    public enum TokenType {
        WORD, EMPTY_LINE, KEYWORD_ON, KEYWORD_WITH;
    }

    public static class Token {

        public final TokenType type;
        public final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s [%s]", this.type, this.value
                    .replaceAll("\r", "\\\\r")
                    .replaceAll("\n", "\\\\n"));
        }
    }

}
