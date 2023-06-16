package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

public class LexerModeRoot extends LexerMode {

    @Override
    public void process(TSLLexerState state, char prevCharacter, char character, char nextCharacter) {
        if (Character.isSpaceChar(character)) {
            state.pushToken();
            return;
        }

        state.pushChar(character);
    }

}
