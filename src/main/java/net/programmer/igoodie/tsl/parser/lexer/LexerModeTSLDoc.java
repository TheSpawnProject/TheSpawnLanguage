package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.parser.token.TSLSymbol;

public class LexerModeTSLDoc extends LexerMode {

    public LexerModeTSLDoc(TSLLexer lexer) {
        super(lexer);
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        char nextCharacter = lexer.getCharacter(1);

        if (character == '*' && nextCharacter == '#') {
            lexer.pushToken();
            lexer.pushCharacters("*#");

            if (TSLSymbol.equals(lexer.getSnippetBuffer().getTokens().get(0), TSLSymbol.Type.TSLDOC_BEGIN)) {
                lexer.pushSnippet();
            } else {
                lexer.pushToken();
            }

            return LexResult.changeMode(new LexerModePlainWord(lexer));
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
