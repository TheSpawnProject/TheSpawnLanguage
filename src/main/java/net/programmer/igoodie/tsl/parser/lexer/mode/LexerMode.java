package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

public abstract class LexerMode {

    public abstract void process(TSLLexerState state, char prevCharacter, char character, char nextCharacter);

}
