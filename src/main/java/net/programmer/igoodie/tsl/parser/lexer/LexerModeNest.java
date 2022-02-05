package net.programmer.igoodie.tsl.parser.lexer;

public class LexerModeNest extends LexerMode {

    private boolean inExpression = false;
    private boolean inGroup = false;
    private int nestLevel = 0;

    private LexerMode subMode;

    public LexerModeNest(TSLLexer lexer) {
        super(lexer);
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        char prevCharacter = lexer.getCharacter(-1);
        char nextCharacter = lexer.getCharacter(1);

        if (character == '%' && prevCharacter != '\\') {
            if (!inExpression) {
                inGroup = !inGroup;
                lexer.pushCharacter(character);
                return LexResult.nothing();
            }
        }

        if (character == '$' && nextCharacter == '{' && prevCharacter != '\\') {
            if (!inGroup && !inExpression) {
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

        if (character == '(') {
            if (!inGroup) {
                nestLevel++;
            }
        }

        if (character == ')') {
            if (!inGroup) {
                if (nestLevel == 0) {
                    lexer.pushCharacter(')');
                    return LexResult.changeMode(new LexerModeString(lexer));
                } else {
                    nestLevel--;
                }
            }
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
