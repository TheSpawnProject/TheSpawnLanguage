package net.programmer.igoodie.legacy.parser.lexer;

abstract class LexerMode {

    protected TSLLexer lexer;

    public LexerMode(TSLLexer lexer) {
        this.lexer = lexer;
    }

    public abstract LexResult step(int lineNo, int characterNo, char character);

}
