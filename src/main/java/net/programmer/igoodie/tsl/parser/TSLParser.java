package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.interpreter.TSLWordInterpreter;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TSLParser {

    protected final TokenStream tokenStream;
    protected final TSLParserImpl parserImpl;

    protected TSLParser(CharStream charStream) {
        this(new CommonTokenStream(new TSLLexer(charStream)));
    }

    protected TSLParser(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
        this.parserImpl = new TSLParserImpl(tokenStream);
    }

    public List<Token> readAllTokens() {
        if (this.tokenStream instanceof BufferedTokenStream bts) {
            bts.fill();
            return bts.getTokens();
        }

        throw new IllegalStateException("Token stream does not support fetching of all tokens at once.");
    }

    public List<TSLWord> parseWords() {
        TSLParserImpl.TslWordsContext ast = this.parserImpl.tslWords();
        TSLWordInterpreter interpreter = new TSLWordInterpreter();
        return ast.word().stream().map(interpreter::interpret).toList();
    }

    public static TSLParser fromScript(String script) {
        return new TSLParser(CharStreams.fromString(script));
    }

    public static TSLParser fromFile(File file) throws IOException {
        return new TSLParser(CharStreams.fromFileName(file.getAbsolutePath()));
    }

    public static TSLParser fromTokens(List<Token> tokens) {
        return new TSLParser(new CommonTokenStream(new ListTokenSource(tokens)));
    }

}
