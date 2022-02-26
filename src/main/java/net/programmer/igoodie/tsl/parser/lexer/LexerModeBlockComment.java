package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.parser.token.TSLSymbol;

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

            if (TSLSymbol.equals(lexer.getTokenBuffer().getTokens().get(0), TSLSymbol.Type.MULTI_LINE_COMMENT_BEGIN)) {
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
