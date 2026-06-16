package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.token.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Collections;
import java.util.List;

public class TSLTokenInterpreter extends TSLInterpreter<TSLToken, TSLParserImpl.WordContext> {

    protected TSLToken tslToken;

    @Override
    protected TSLToken yieldValue(TSLParserImpl.WordContext tree) {
        return this.tslToken;
    }

    public TSLToken interpretToken(Token token) {
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
    public TSLToken visitGroup(TSLParserImpl.GroupContext ctx) {
        TSLGroupInterpreter interpreter = new TSLGroupInterpreter();
        return (this.tslToken = interpreter.interpret(ctx));
    }

    @Override
    public TSLToken visitCaptureCall(TSLParserImpl.CaptureCallContext ctx) {
        TSLCaptureId captureId = (TSLCaptureId) this.interpretToken(ctx.id);

        TSLParserImpl.CaptureArgsContext captureArgsCtx = ctx.captureArgs();

        List<TSLParserImpl.CaptureArgContext> captureArgs = captureArgsCtx == null
                ? Collections.emptyList() : captureArgsCtx.captureArg();

        List<TSLClause> arguments = captureArgs.stream().map(captureArg -> {
            ParseTree child = captureArg.children.get(0);

            if (child instanceof TSLParserImpl.WordContext wordChild) {
                return new TSLTokenInterpreter().interpret(wordChild);

            } else if (child instanceof TSLParserImpl.WordNestContext nestChild) {
                return new TSLActionNestInterpreter().interpret(nestChild.wordNestContent());
            }

            throw new TSLInternalException("Interpreted a capture arg as something other than word or nest. How?");
        }).toList();

        return (this.tslToken = new TSLCaptureCall(captureId, arguments).setSource(ctx));
    }

    @Override
    public TSLToken visitTerminal(TerminalNode node) {
        Token token = node.getSymbol();

        TSLToken tslToken = interpretToken(token);

        if (tslToken == null) {
            throw new TSLInternalException("Unknown tslToken type {}",
                    TSLLexer.VOCABULARY.getDisplayName(token.getType()));
        }

        return (this.tslToken = tslToken);
    }

}
