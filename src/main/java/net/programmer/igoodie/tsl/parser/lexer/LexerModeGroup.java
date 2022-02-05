package net.programmer.igoodie.tsl.parser.lexer;

public class LexerModeGroup extends LexerMode {

    private boolean inExpression = false;
    private LexerMode subMode;

    public LexerModeGroup(TSLLexer lexer) {
        super(lexer);
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        char prevCharacter = lexer.getCharacter(-1);
        char nextCharacter = lexer.getCharacter(1);

        if (character == '%' && prevCharacter != '\\') {
            if (!inExpression) {
                lexer.pushCharacter(character);
                return LexResult.changeMode(new LexerModeString(lexer));
            }
        }

        if (character == '$' && nextCharacter == '{' && prevCharacter != '\\') {
            if (!inExpression) {
                inExpression = true;
                subMode = new LexerModeExpression(lexer);
                lexer.pushCharacters("${");
                return LexResult.nothing();
            }
        }

        if (inExpression) {
            LexResult result = subMode.step(lineNo, characterNo, character);
            if (result.getChangeMode() != null) {
                inExpression = false;
                subMode = null;
            }
            return LexResult.nothing();
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
