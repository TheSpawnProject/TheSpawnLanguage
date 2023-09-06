package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.lexer.mode.LexerMode;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TSLLexer {

    protected TSLLexerState state;
    protected List<TSLUnparsedSnippet> snippets;

    public TSLLexer(String tslScript) {
        this.state = new TSLLexerState(this, tslScript.split("\r?\n"));
        this.snippets = new ArrayList<>();
    }

    public TSLLexer(TSLLexerState initialState) {
        this.state = initialState;
        this.snippets = new ArrayList<>();
    }

    public List<TSLUnparsedSnippet> lexAll() {
        return lexUntil(null);
    }

    public List<TSLUnparsedSnippet> lexUntil(@Nullable Predicate<TSLLexerState> until) {
        lineLoop:
        for (; state.scanningLine < state.lines.length; state.scanningLine++) {
            if (state.getCurrentLine().trim().isEmpty()) {
                state.modeStack.peek().handleEmptyLine(this.state);
                continue;
            }

            for (; state.scanningColumn < state.getCurrentLine().length(); state.scanningColumn++) {
                if (until != null && until.test(state)) {
                    break lineLoop;
                }

                System.out.println("Processing @ " + state.scanningLine + " " + state.scanningColumn
                        + " " + state.getCharacterByOffset(0) + " " + (until == null ? "Main" : "Sub")
                        + " " + state.hashCode() + " " + state.modeStack);

                int result = state.modeStack.peek().process(this.state);

                if (result == LexerMode.SKIP_LINE) {
                    state.modeStack.peek().handleEndOfLine(state);
                    break;
                }

                if (state.scanningColumn == state.getCurrentLine().length() - 1) {
                    state.modeStack.peek().handleEndOfLine(state);
                }
            }

            state.scanningColumn = 0;
        }

        if (state.modeStack.size() != 1) {
            throw new TSLSyntaxError("Incompleted token")
                    .at(state.scanningLine, state.scanningColumn);
        }

        System.out.println("Finishing " + state.hashCode());

        state.pushToken();
        state.finalizeSnippet();
        return snippets;
    }

    public List<TSLUnparsedSnippet> getSnippets() {
        return snippets;
    }

}
