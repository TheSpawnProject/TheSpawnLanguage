package net.programmer.igoodie.tsl.parser.lexer.mode;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexerState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LexerModeExpression extends LexerMode {

    protected static final Set<Character> VALID_STRING_CHARS = new HashSet<>(Arrays.asList('\'', '"', '`'));

    protected boolean escaping = false;
    protected Stack<String> scopeStack = new Stack<>();

    @Override
    public int process(TSLLexerState state) {
        char character = state.getCharacterByOffset(0);
        char nextCharacter = state.getCharacterByOffset(1);

        if (character == '\\') {
            state.pushChars("\\");
            escaping = !escaping;
            return CONTINUE;
        }

        if (VALID_STRING_CHARS.contains(character)) {
            if (escaping) {
                state.pushChars(character);
                escaping = false;
                return CONTINUE;
            }
            if (getScope().charAt(0) == character) {
                state.pushChars(character);
                scopeStack.pop();
                return CONTINUE;
            }
            if (!VALID_STRING_CHARS.contains(getScope().charAt(0))) {
                state.pushChars(character);
                scopeStack.push(String.valueOf(character));
                return CONTINUE;
            }
        }

        if (character == '$' && nextCharacter == '{') {
            state.pushChars("${");
            if (escaping) {
                escaping = false;
            } else {
                scopeStack.push("${");
            }
            return CONTINUE;
        }

        if (character == '{') {
            if (!escaping) {
                if (!VALID_STRING_CHARS.contains(getScope().charAt(0))) {
                    scopeStack.push("{");
                    state.pushChars("{");
                    return CONTINUE;
                }
            }
        }

        if (character == '}') {
            if (scopeStack.isEmpty()) {
                if (!escaping) {
                    state.pushChars('}');
                    state.pushToken();
                    state.popMode();
                    return CONTINUE;
                }
            }
            if (getScope().equals("${") || getScope().equals("{")) {
                state.pushChars('}');
                scopeStack.pop();
                return CONTINUE;
            }
        }

        state.pushChars(character);
        if (escaping) escaping = false; // Dangling escape
        return CONTINUE;
    }

    protected String getScope() {
        if (scopeStack.isEmpty()) return " ";
        return scopeStack.peek();
    }

}
