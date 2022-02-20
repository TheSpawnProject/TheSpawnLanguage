package net.programmer.igoodie.tsl.parser.lexer;

public class LexerModeExpression extends LexerMode {

    private boolean inSingleQuote = false;
    private boolean inDoubleQuote = false;
    private int curlyLevel = 0;

    public LexerModeExpression(TSLLexer lexer) {
        super(lexer);
    }

    private boolean inString() {
        return inSingleQuote || inDoubleQuote;
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        if (character == '\'') {
            if (!inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
                lexer.pushCharacter('\'');
                return LexResult.nothing();
            }
        }

        if (character == '\"') {
            if (!inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
                lexer.pushCharacter('\"');
                return LexResult.nothing();
            }
        }

        if (character == '{' && !inString()) {
            curlyLevel++;
        }

        if (character == '}' && !inString()) {
            if (curlyLevel == 0) {
                lexer.pushCharacter('}');
                return LexResult.merge(LexResult.pushToken(),
                        LexResult.changeMode(new LexerModePlainWord(lexer)));

            } else {
                curlyLevel--;
                lexer.pushCharacter('}');
                return LexResult.nothing();
            }
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
