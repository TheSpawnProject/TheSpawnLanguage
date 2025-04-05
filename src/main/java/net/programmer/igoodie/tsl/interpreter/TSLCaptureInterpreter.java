package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class TSLCaptureInterpreter extends TSLInterpreter<TSLCapture, TSLParserImpl.CaptureRuleContext> {

    protected TSLCaptureId id;
    protected List<String> params;
    protected List<TSLWord> contents;

    @Override
    public TSLCapture yieldValue(TSLParserImpl.CaptureRuleContext tree) {
        return new TSLCapture(id, params, contents);
    }

    @Override
    public TSLCapture visitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx) {
        this.id = (TSLCaptureId) new TSLWordInterpreter().interpretToken(ctx.id);
        this.params = ctx.captureParams().IDENTIFIER().stream()
                .map(ParseTree::getText)
                .toList();
        return null;
    }

    @Override
    public TSLCapture visitActionArgs(TSLParserImpl.ActionArgsContext ctx) {
        return super.visitActionArgs(ctx);
    }

}
