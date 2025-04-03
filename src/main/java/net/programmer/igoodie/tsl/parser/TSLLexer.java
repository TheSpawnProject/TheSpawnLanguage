package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;

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

    protected int lineNo = 0;
    protected int charNo = 0;

    protected StringBuilder sb = new StringBuilder();

    public TSLLexer(CharStream charStream) {
        this.charStream = charStream;
    }

    protected void consumeChar() throws IOException {
        this.consumeChar(1);
    }

    protected void consumeChar(int k) throws IOException {
        this.charStream.consume(k);
        charNo += k;
    }

    public List<Token> tokenize() throws IOException, TSLSyntaxException {
        List<Token> tokens = new ArrayList<>();

        while (charStream.hasNext()) {
            char curr = charStream.peek();

            if (inSingleComment) {
                if (curr == '\n') {
                    inSingleComment = false;
                    charNo = 0;
                    lineNo++;
                } else {
                    consumeChar();
                    continue;
                }
            }

            if (inMultiComment) {
                if (curr == '*' && charStream.peek(2) == '#') {
                    inMultiComment = false;
                    consumeChar(2);
                } else {
                    consumeChar();
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
                    consumeChar();
                    charNo = 0;
                    lineNo++;
                } else {
                    if (!sb.isEmpty()) {
                        tokens.add(generateToken());
                        sb.setLength(0);
                    }
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
                    consumeChar();
                    isEscaping = false;
                    continue;
                }

                if (curr == '\\') {
                    isEscaping = true;
                    consumeChar();
                    continue;
                }

                if (curr == '%') {
                    tokens.add(generateToken());
                    sb.setLength(0);
                    inGroup = false;
                    consumeChar();
                    continue;
                }

                sb.append(curr);
                consumeChar();
                continue;
            }

            // Handle CRLF
            if (curr == '\r' && charStream.peek(2) == '\n') {
                if (!sb.isEmpty()) {
                    tokens.add(generateToken());
                    sb.setLength(0);
                    charNo = 0;
                    lineNo++;
                }
                prevNewLine = true;
                consumeChar(2);
                continue;
            }

            // Handle LF or CR
            if (curr == '\n' || curr == '\r') {
                if (!sb.isEmpty()) {
                    tokens.add(generateToken());
                    sb.setLength(0);
                    charNo = 0;
                    lineNo++;
                }
                prevNewLine = true;
                consumeChar();
                continue;
            }

            if (curr == '%') {
                inGroup = true;
                consumeChar();
                continue;
            }

            if (curr == ' ') {
                if (!sb.isEmpty()) {
                    tokens.add(generateToken());
                    sb.setLength(0);
                }
                consumeChar();
                continue;
            }

            sb.append(curr);
            consumeChar();
        }

        if (inGroup) throw new TSLSyntaxException("Unclosed group");
        if (isEscaping) throw new TSLSyntaxException("Unexpected escaping");

        if (!sb.isEmpty()) {
            tokens.add(generateToken());
        }

        return tokens;
    }

    protected Token generateToken() {
        if (prevNewLine) return new Token(TokenType.EMPTY_LINE, sb.toString(), lineNo, charNo);
        return generateToken(sb.toString(), this.lineNo, this.charNo);
    }

    public static Token generateToken(String sequence, int lineNo, int charNo) {
        if (sequence.equalsIgnoreCase("ON")) return new Token(TokenType.KEYWORD_ON, sequence, lineNo, charNo);
        if (sequence.equalsIgnoreCase("WITH")) return new Token(TokenType.KEYWORD_WITH, sequence, lineNo, charNo);
        return new Token(TokenType.WORD, sequence, lineNo, charNo);
    }

    public enum TokenType {
        WORD, EMPTY_LINE, KEYWORD_ON, KEYWORD_WITH;
    }

    public static class Token {

        public final TokenType type;
        public final String value;
        public final int lineNo, charNo;

        public Token(TokenType type, String value, int line, int charNo) {
            this.type = type;
            this.value = value;
            this.lineNo = line;
            this.charNo = charNo;
        }

        @Override
        public String toString() {
            return String.format("%s [%s]", this.type, this.value
                    .replaceAll("\r", "\\\\r")
                    .replaceAll("\n", "\\\\n"));
        }
    }

}
