package net.programmer.igoodie.tsl.parser.lexer;

import net.programmer.igoodie.tsl.parser.lexer.mode.LexerMode;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;

import java.util.ArrayList;
import java.util.List;

public class TSLLexer {

    protected TSLLexerState state;
    protected List<TSLUnparsedSnippet> snippets;

    public TSLLexer(String tslScript) {
        this.state = new TSLLexerState(this, tslScript.split("\r?\n"));
        this.snippets = new ArrayList<>();
    }

    public void lex() {
        for (state.scanningLine = 0; state.scanningLine < state.lines.length; state.scanningLine++) {
            String line = state.getCurrentLine();

            if (line.trim().isEmpty()) {
                state.modeStack.peek().handleEmptyLine(this.state);
                continue;
            }

            for (state.scanningColumn = 0; state.scanningColumn < line.length(); state.scanningColumn++) {
                int result = state.modeStack.peek().process(this.state);

                if (result == LexerMode.SKIP_LINE) {
                    state.modeStack.peek().handleEndOfLine(state);
                    break;
                }

                if (state.scanningColumn == line.length() - 1) {
                    state.modeStack.peek().handleEndOfLine(state);
                }
            }
        }

        state.pushToken();
        state.finalizeSnippet();
    }

    public List<TSLUnparsedSnippet> getSnippets() {
        return snippets;
    }

}
