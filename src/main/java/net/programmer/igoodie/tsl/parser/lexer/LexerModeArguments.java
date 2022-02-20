package net.programmer.igoodie.tsl.parser.lexer;

class LexerModeArguments extends LexerMode {

    private boolean inExpression = false;
    private boolean inGroup = false;
    private boolean inNest = false;
    private LexerMode subMode;

    public LexerModeArguments(TSLLexer lexer) {
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

        if (inNest) {
            LexResult result = subMode.step(lineNo, characterNo, character);
            if (result.getChangeMode() != null) {
                inNest = false;
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

        if (character == '(' && prevCharacter != '\\') {
            inNest = true;
            subMode = new LexerModeNest(lexer);
            lexer.pushCharacter('(');
            return LexResult.nothing();
        }

        if (character == ')' && prevCharacter != '\\') {
            lexer.pushCharacter(')');
            return LexResult.changeMode(new LexerModePlainWord(lexer));
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
