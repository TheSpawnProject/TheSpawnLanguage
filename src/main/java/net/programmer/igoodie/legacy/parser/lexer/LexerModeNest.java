package net.programmer.igoodie.legacy.parser.lexer;

public class LexerModeNest extends LexerMode {

    private boolean inExpression = false;
    private boolean inGroup = false;
    private LexerMode subMode;
    private int nestLevel = 0;

    public LexerModeNest(TSLLexer lexer) {
        super(lexer);
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        char prevCharacter = lexer.getCharacter(-1);
        char nextCharacter = lexer.getCharacter(1);

        if (inExpression) {
            LexResult result = subMode.step(lineNo, characterNo, character);
            if (result.getChangeMode() != null) {
                inExpression = false;
                subMode = null;
            }
            return result.revertChangeMode().revertPushToken();
        }

        if (inGroup) {
            LexResult result = subMode.step(lineNo, characterNo, character);
            if (result.getChangeMode() != null) {
                inGroup = false;
                subMode = null;
            }
            return result.revertChangeMode().revertPushToken();
        }

        if (character == '%' && prevCharacter != '\\') {
            inGroup = true;
            subMode = new LexerModeGroup(lexer);
            lexer.pushCharacter('%');
            return LexResult.nothing();
        }

        if (character == '$' && nextCharacter == '{' && prevCharacter != '\\') {
            inExpression = true;
            subMode = new LexerModeExpression(lexer);
            lexer.pushCharacters("${");
            return LexResult.nothing();
        }

        if (character == '(') {
            nestLevel++;
        }

        if (character == ')') {
            if (nestLevel == 0) {
                lexer.pushCharacter(')');
                return LexResult.changeMode(new LexerModePlainWord(lexer));
            } else {
                nestLevel--;
            }
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
