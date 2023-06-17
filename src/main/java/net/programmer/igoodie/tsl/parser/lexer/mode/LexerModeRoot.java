package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

public class LexerModeRoot extends LexerMode {

    @Override
    public void handleEndOfLine(TSLLexerState state) {
        state.pushToken();
    }

    @Override
    public void handleEmptyLine(TSLLexerState state) {
        state.finalizeSnippet();
    }

    @Override
    public int process(TSLLexerState state) {
        char character = state.getCharacterByOffset(0);
        char nextCharacter = state.getCharacterByOffset(1);

        if (character == '#') {
            if (nextCharacter == '*') {
                if (state.getCharacterByOffset(2) == '*') {
                    state.pushToken();
                    state.pushSnippet();
                    state.pushChars("#**");
                    state.pushToken();
                    state.pushMode(new LexerModeDoc());
                    return CONTINUE;
                }
                state.pushToken();
                state.skipCharacters("*");
                state.pushMode(new LexerModeComment());
                return CONTINUE;
            }
            if (nextCharacter != '!')
                return SKIP_LINE;
        }

        if (Character.isSpaceChar(character)) {
            state.pushToken();
            return CONTINUE;
        }

        if (character == '(') {
            state.pushSnippet();
            return CONTINUE;
        }

        if (character == ')') {
            state.pushToken();
            state.popSnippet();
            return CONTINUE;
        }

        // TODO Keep implementing from here

        state.pushChar(character);
        return CONTINUE;
    }

}
