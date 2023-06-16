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
        for (; state.scanningLine < state.lines.length; state.scanningLine++) {
            String line = state.getCurrentLine();

            for (; state.scanningColumn < line.length(); state.scanningColumn++) {
                char prevCharacter = state.getCharacterByOffset(-1);
                char character = state.getCharacterByOffset(0);
                char nextCharacter = state.getCharacterByOffset(1);

                LexerMode mode = state.modeStack.peek();
                mode.process(this.state, prevCharacter, character, nextCharacter);
            }
        }

        state.pushToken();
        state.finalizeSnippet();
    }

    public List<TSLUnparsedSnippet> getSnippets() {
        return snippets;
    }

}
