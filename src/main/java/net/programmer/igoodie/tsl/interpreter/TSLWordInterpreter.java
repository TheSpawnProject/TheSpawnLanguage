package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.word.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Collections;
import java.util.List;

public class TSLWordInterpreter extends TSLInterpreter<TSLWord, TSLParserImpl.WordContext> {

    protected TSLWord word;

    @Override
    public TSLWord yieldValue(TSLParserImpl.WordContext tree) {
        return this.word;
    }

    @Override
    public TSLWord visitGroup(TSLParserImpl.GroupContext ctx) {
        TSLGroupInterpreter interpreter = new TSLGroupInterpreter();
        return (this.word = interpreter.interpret(ctx));
    }

    @Override
    public TSLWord visitCaptureCall(TSLParserImpl.CaptureCallContext ctx) {
        TSLCaptureId captureId = (TSLCaptureId) this.interpretToken(ctx.id);

        TSLParserImpl.CaptureArgsContext captureArgsCtx = ctx.captureArgs();
        List<TSLParserImpl.WordContext> wordCtx = captureArgsCtx == null ? Collections.emptyList() : captureArgsCtx.word();

        List<TSLWord> arguments = wordCtx.stream().map(this::interpret).toList();

        return (this.word = new TSLCaptureCall(captureId, arguments).setSource(ctx));
    }

    protected TSLWord interpretToken(Token token) {
        String text = token.getText();

        if (token.getType() == TSLLexer.PLACEHOLDER) {
            return new TSLPlaceholder(text.substring(2, text.length() - 2))
                    .setSource(token);
        }

        if (token.getType() == TSLLexer.CAPTURE_IDENTIFIER) {
            return new TSLCaptureId(text.substring(1))
                    .setSource(token);
        }

        if (token.getType() == TSLLexer.EXPRESSION) {
            return new TSLExpression(text.substring(2, text.length() - 1))
                    .setSource(token);
        }

        if (token.getType() == TSLLexer.PLAIN_WORD
                || token.getType() == TSLLexer.IDENTIFIER) {
            return new TSLPlainWord(text)
                    .setSource(token);
        }

        return null;
    }

    @Override
    public TSLWord visitTerminal(TerminalNode node) {
        Token token = node.getSymbol();

        TSLWord word = interpretToken(token);

        if (word == null) {
            throw new TSLInternalException("Unknown word type {}",
                    TSLLexer.VOCABULARY.getDisplayName(token.getType()));
        }

        return (this.word = word);
    }

}
