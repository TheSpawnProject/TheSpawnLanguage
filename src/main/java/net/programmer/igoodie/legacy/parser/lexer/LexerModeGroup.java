package net.programmer.igoodie.legacy.parser.lexer;

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

        if (character == '\\') {
            if(nextCharacter == '%' || nextCharacter == '$' || nextCharacter == '\\') {
                lexer.pushCharacter('\\');
                lexer.pushCharacter(nextCharacter);
                lexer.skipCharacters(1);
                return LexResult.nothing();
            }
        }

        if (character == '%') {
            if (!inExpression) {
                lexer.pushCharacter('%');
                return LexResult.merge(LexResult.pushToken(),
                        LexResult.changeMode(new LexerModePlainWord(lexer)));
            }
        }

        if (character == '$' && nextCharacter == '{') {
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
