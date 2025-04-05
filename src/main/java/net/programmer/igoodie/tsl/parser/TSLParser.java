package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.interpreter.TSLWordInterpreter;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TSLParser {

    protected final CharStream charStream;
    protected final TSLLexer lexer;
    protected final CommonTokenStream tokenStream;
    protected final TSLParserImpl parser;

    protected TSLParser(CharStream charStream) {
        this.charStream = charStream;
        this.lexer = new TSLLexer(this.charStream);
        this.tokenStream = new CommonTokenStream(this.lexer);
        this.parser = new TSLParserImpl(tokenStream);
    }

    public List<Token> getTokens() {
        this.tokenStream.fill();
        return this.tokenStream.getTokens();
    }

    public List<TSLWord> parseWords() {
        TSLParserImpl.TslWordsContext ast = this.parser.tslWords();
        TSLWordInterpreter interpreter = new TSLWordInterpreter();
        return ast.word().stream().map(interpreter::interpret).toList();
    }

    public static TSLParser fromScript(String script) {
        return new TSLParser(CharStreams.fromString(script));
    }

    public static TSLParser fromFile(File file) throws IOException {
        return new TSLParser(CharStreams.fromFileName(file.getAbsolutePath()));
    }

}
