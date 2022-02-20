package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

class LexerModePlainWord extends LexerMode {

    public LexerModePlainWord(TSLLexer lexer) {
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
                if (lexer.getCharacter(2) == '*') {
                    lexer.pushToken();
                    lexer.pushCharacters("#**");
                    lexer.pushToken();
                    return LexResult.changeMode(new LexerModeTSLDoc(lexer));

                } else {
                    lexer.pushToken();
                    lexer.pushCharacters("#*");
                    lexer.pushToken();
                    return LexResult.changeMode(new LexerModeBlockComment(lexer));
                }

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

        if (lexer.isUsingCommaDelimiter()) {
            if (prevCharacter == ' ' && character != ',') {
                if (!lexer.accumulatedString().trim().isEmpty()) {
                    throw new TSLSyntaxError("Unexpected space between arguments", lineNo, characterNo);
                }
            }
        }

        lexer.pushCharacter(character);
        return LexResult.nothing();
    }

}
