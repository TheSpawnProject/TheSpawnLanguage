package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.interpreter.TSLActionInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLCaptureInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLRulesetInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLTokenInterpreter;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
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

    public List<TSLToken> parseTokens() {
        TSLParserImpl.TslWordsContext wordsTree = this.parserImpl.tslWords();
        TSLTokenInterpreter interpreter = new TSLTokenInterpreter();
        return wordsTree.word().stream().map(interpreter::interpret).toList();
    }

    public TSLAction.Deferred parseAction() {
        TSLParserImpl.ActionContext actionTree = this.parserImpl.action();
        return new TSLActionInterpreter().interpret(actionTree);
    }

    public TSLCapture parseCapture() {
        TSLParserImpl.CaptureRuleContext captureTree = this.parserImpl.captureRule();
        return new TSLCaptureInterpreter().interpret(captureTree);
    }

    public TSLDeferred<TSLRuleset> parseRuleset() {
        TSLParserImpl.TslRulesetContext rulesetTree = this.parserImpl.tslRuleset();
        return new TSLRulesetInterpreter().interpret(rulesetTree);
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
