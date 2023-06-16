package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

public abstract class LexerMode {

    public static int CONTINUE = 0;
    public static int SKIP_LINE = 1;

    public abstract int process(TSLLexerState state);

    public void handleEndOfLine(TSLLexerState state) {}

    public void handleEmptyLine(TSLLexerState state) {}

}
