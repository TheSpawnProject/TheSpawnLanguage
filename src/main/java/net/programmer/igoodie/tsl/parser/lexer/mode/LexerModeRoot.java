package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;

public class LexerModeRoot extends LexerMode {

    protected boolean escaping = false;

    @Override
    public void handleEndOfLine(TSLLexerState state) {
        if (escaping) {
            throw new TSLSyntaxError("Incomplete escaping sequence")
                    .at(state.getScanningLine(), state.getScanningColumn());
        }

        state.pushToken();
    }

    @Override
    public void handleEmptyLine(TSLLexerState state) {
        state.finalizeSnippet();
    }

    @Override
    public int process(TSLLexerState state) {
        char character = state.getCharacterByOffset(0);
        char nextCharacter = state.getCharacterByOffset(1);

        if (character == '#') {
            if (nextCharacter == '*') {
                if (state.getCharacterByOffset(2) == '*') {
                    state.pushToken();
                    state.pushSnippet();
                    state.pushChars("#**");
                    state.pushToken();
                    state.pushMode(new LexerModeDoc());
                    return CONTINUE;
                }
                state.pushToken();
                state.skipCharacters("*");
                state.pushMode(new LexerModeComment());
                return CONTINUE;
            }
            if (nextCharacter != '!')
                return SKIP_LINE;
        }

        if (Character.isSpaceChar(character)) {
            state.pushToken();
            return CONTINUE;
        }

        if (character == '(') {
            state.pushSnippet();
            return CONTINUE;
        }

        if (character == ')') {
            state.pushToken();
            state.popSnippet();
            return CONTINUE;
        }

        if (character == '\\') {
            if (escaping) {
                state.pushChars('\\');
                return CONTINUE;
            }
            if (state.getAccumulatedToken().length() == 0) state.markBegunWhileEscaping();
            escaping = true;
            return CONTINUE;
        }

        for (TSLSymbol.Type symbolType : TSLSymbol.Type.values()) {
            if (matchAhead(state, symbolType.getSymbol())) {
                if (escaping) {
                    state.pushChars(symbolType.getSymbol());
                    escaping = false;
                    return CONTINUE;
                }
            }
        }

        if (character == '$' && nextCharacter == '{') {
            if (escaping) {
                state.pushChars("${");
                escaping = false;
                return CONTINUE;
            }
            if (state.getAccumulatedToken().length() != 0) {
                throw new TSLSyntaxError("Expected a space between previous token")
                        .at(state.getScanningLine(), state.getScanningColumn());
            }
            state.pushChars("${");
            state.pushMode(new LexerModeExpression());
            return CONTINUE;
        }

        // TODO Keep implementing from here

        if (escaping) {
            throw new TSLSyntaxError("Invalid escaping sequence -> \\{}", character)
                    .at(state.getScanningLine(), state.getScanningColumn());
        }

        state.pushChars(character);
        return CONTINUE;
    }

    private boolean matchAhead(TSLLexerState state, String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            char character = state.getCharacterByOffset(i);
            if (character != pattern.charAt(i))
                return false;
        }
        return true;
    }

}
