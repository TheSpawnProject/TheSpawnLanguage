package net.programmer.igoodie.parser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

// DROP apple 1\nON Twitch Donation\n\n
public class TSLLexer {

    // Word -> DROP
    // Word -> ${something}
    // Word -> minecraft:stick
    // Group -> %Some ${something}, words with space%
    // Group -> %Some \${escaped}%
    // Empty Line -> \n\n

    public List<Token> tokenize(CharStream charStream) throws IOException {

        return Collections.emptyList();
    }

    public enum TokenType {
        WORD, GROUP, EMPTY_LINE
    }

    public static class Token {

        public final TokenType type;

        public Token(TokenType type) {
            this.type = type;
        }

    }

}
