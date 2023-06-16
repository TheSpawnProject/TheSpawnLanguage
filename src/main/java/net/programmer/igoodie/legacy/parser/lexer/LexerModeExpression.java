package net.programmer.igoodie.legacy.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LexerModeExpression extends LexerMode {

    private static final char BACKTICK = '`';
    private static final char APOSTROPHE = '\'';
    private static final char QUOTATION = '"';
    private static final Set<Character> QUOTE_CHARS = new HashSet<>(Arrays.asList(APOSTROPHE, QUOTATION, BACKTICK));

    // ${`${"Hi`"}`}

    private final Stack<Character> stringStack = new Stack<>();
    private int templateExprLevel = 0;
    private int curlyLevel = 0;

    public LexerModeExpression(TSLLexer lexer) {
        super(lexer);
    }

    private boolean inString() {
        return !stringStack.isEmpty();
    }

    private char getLatestQuote() {
        return stringStack.isEmpty() ? 0 : stringStack.peek();
    }

    @Override
    public LexResult step(int lineNo, int characterNo, char character) {
        // TODO: Make dis work:
        // `Hi there ${true ? "Buddy`o Dood!" : "Friend'\"'"}. How are ya?`

        char nextCharacter = lexer.getCharacter(1);

        if (character == '$' && nextCharacter == '{') {
            if (getLatestQuote() == BACKTICK) {
                if (templateExprLevel == 0) {
                    throw new TSLSyntaxError("Unexpected expression beginning").at(lineNo, characterNo);
                }
                lexer.pushCharacters("${");
                return LexResult.nothing();
            }
        }

        if (character == '}') {
            if (getLatestQuote() == BACKTICK) {
                lexer.pushCharacter(character);
                return LexResult.nothing();
            }
        }

        if (QUOTE_CHARS.contains(character)) {
            if (character == getLatestQuote()) {
                stringStack.pop();
            }
            lexer.pushCharacter(character);
            return LexResult.nothing();
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
