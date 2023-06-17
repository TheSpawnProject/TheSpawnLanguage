package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

public class LexerModeDoc extends LexerMode {

    @Override
    public void handleEndOfLine(TSLLexerState state) {
        state.pushToken();
    }

    @Override
    public int process(TSLLexerState state) {
        char character = state.getCharacterByOffset(0);
        char nextCharacter = state.getCharacterByOffset(1);

        if (character == ' ') {
            state.pushToken();
            return CONTINUE;
        }

        if (character == '*' && nextCharacter == '#') {
            state.pushToken();
            state.pushChars("*#");
            state.pushToken();
            state.popSnippet();
            state.popMode();
            return CONTINUE;
        }

        state.pushChar(character);
        return CONTINUE;
    }

}
