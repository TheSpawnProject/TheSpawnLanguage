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
                LexerMode mode = state.modeStack.peek();
                mode.handleEmptyLine(this.state);
                continue;
            }

            for (state.scanningColumn = 0; state.scanningColumn < line.length(); state.scanningColumn++) {
                LexerMode mode = state.modeStack.peek();
                int result = mode.process(this.state);

                if (result == LexerMode.SKIP_LINE) {
                    mode.handleEndOfLine(state);
                    break;
                }

                if (state.scanningColumn == line.length() - 1) {
                    mode.handleEndOfLine(state);
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
