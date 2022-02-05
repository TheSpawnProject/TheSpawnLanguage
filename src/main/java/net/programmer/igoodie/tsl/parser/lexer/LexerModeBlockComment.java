package net.programmer.igoodie.tsl.parser.lexer;

public class LexerModeBlockComment extends LexerMode {

    public LexerModeBlockComment(TSLLexer lexer) {
        super(lexer);
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        char nextCharacter = lexer.getCharacter(1);

        if (character == '*' && nextCharacter == '#') {
            lexer.pushToken();
            lexer.pushCharacters("*#");
            lexer.pushToken();
            return LexResult.changeMode(new LexerModeString(lexer));
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
