package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLTokenNest;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSLCaptureInterpreter extends TSLInterpreter<TSLCapture, TSLParserImpl.CaptureRuleContext> {

    protected TSLCaptureId id;
    protected List<String> params;
    protected List<TSLClause> contents;

    @Override
    protected TSLCapture yieldValue(TSLParserImpl.CaptureRuleContext tree) {
        return new TSLCapture(this.id, this.params, this.contents);
    }

    @Override
    public TSLCapture visitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx) {
        this.id = (TSLCaptureId) new TSLTokenInterpreter().interpretToken(ctx.id);

        TSLParserImpl.CaptureParamsContext captureParamsTree = ctx.captureParams();

        this.params = captureParamsTree == null
                ? Collections.emptyList()
                : captureParamsTree.IDENTIFIER().stream()
                .map(ParseTree::getText)
                .toList();

        return null;
    }

    @Override
    public TSLCapture visitCaptureContent(TSLParserImpl.CaptureContentContext ctx) {
        this.contents = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (child instanceof TSLParserImpl.WordContext wordChild) {
                TSLToken token = new TSLTokenInterpreter().interpret(wordChild);
                this.contents.add(token);

            } else if (child instanceof TSLParserImpl.WordNestContext nestChild) {
                TSLTokenNest tokenNest = new TSLTokenNestInterpreter().interpret(nestChild);
                this.contents.add(tokenNest);
            }
        }

        return null;
    }

}
