package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

class LexerModeString extends LexerMode {

    public LexerModeString(TSLLexer lexer) {
        super(lexer);
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        char prevCharacter = lexer.getCharacter(-1);
        char nextCharacter = lexer.getCharacter(1);

        if (character == '*' && nextCharacter == '#') {
            throw new TSLSyntaxError("Unexpected block comment ending", lineNo, characterNo);
        }

        if (character == '#') {
            if (nextCharacter == '*') {
                lexer.pushToken();
                lexer.pushCharacters("#*");
                lexer.pushToken();
                return LexResult.changeMode(new LexerModeBlockComment(lexer));

            } else if (nextCharacter == '!') {
                lexer.pushCharacters("#!");
                lexer.pushToken();
                return LexResult.nothing();

            } else {
                return LexResult.skipLine();
            }
        }

        if (character == '%' && prevCharacter != '\\') {
            lexer.pushCharacter('%');
            return LexResult.changeMode(new LexerModeGroup(lexer));
        }

        if (character == '$' && nextCharacter == '{' && prevCharacter != '\\') {
            lexer.pushCharacters("${");
            return LexResult.changeMode(new LexerModeExpression(lexer));
        }

        if (character == '(') {
            if (Character.isWhitespace(prevCharacter) || prevCharacter == 0) {
                lexer.pushCharacter('(');
                return LexResult.changeMode(new LexerModeNest(lexer));

            } else if (lexer.accumulatedString().startsWith("$")) {
                lexer.pushCharacter('(');
                return LexResult.changeMode(new LexerModeArguments(lexer));

            } else if (lexer.accumulatedString().startsWith("@")) {
                lexer.pushCharacter('(');
                return LexResult.changeMode(new LexerModeArguments(lexer));

            } else {
                throw new TSLSyntaxError("Unexpected parenthesis character", lineNo, characterNo);
            }
        }

        if (lexer.isUsingCommaDelimiter()) {
            if (character == ' ') {
                return LexResult.nothing();
            }
            if (character == ',') {
                lexer.pushToken();
                return LexResult.nothing();
            }
        } else {
            if (character == ' ') {
                lexer.pushToken();
                return LexResult.nothing();
            }
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
