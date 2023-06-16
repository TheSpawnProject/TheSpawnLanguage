package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

public class LexerModeComment extends LexerMode {

    @Override
    public int process(TSLLexerState state) {
        char prevCharacter = state.getCharacterByOffset(-1);
        char character = state.getCharacterByOffset(0);

        if (prevCharacter == '*' && character == '#') {
            state.popMode();
        }

        return CONTINUE;
    }

}
