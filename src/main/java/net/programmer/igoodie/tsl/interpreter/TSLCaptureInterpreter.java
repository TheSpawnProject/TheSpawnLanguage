package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSLCaptureInterpreter extends TSLInterpreter<TSLDeferred<TSLCapture>, TSLParserImpl.CaptureRuleContext> {

    protected TSLCaptureId id;
    protected List<String> params;
    protected List<Either<TSLWord, TSLDeferred<TSLAction>>> contents;

    @Override
    public TSLDeferred<TSLCapture> yieldValue(TSLParserImpl.CaptureRuleContext tree) {
        return platform -> {
            List<TSLClause> resolvedTemplate = this.contents.stream()
                    .map(deferredArg -> deferredArg.reduce(
                            word -> word,
                            deferredNest -> deferredNest.resolve(platform)
                    ))
                    .toList();

            return new TSLCapture(this.id, this.params, resolvedTemplate);
        };
    }

    @Override
    public TSLDeferred<TSLCapture> visitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx) {
        this.id = (TSLCaptureId) new TSLWordInterpreter().interpretWord(ctx.id);

        TSLParserImpl.CaptureParamsContext captureParamsTree = ctx.captureParams();

        this.params = captureParamsTree == null
                ? Collections.emptyList()
                : captureParamsTree.IDENTIFIER().stream()
                .map(ParseTree::getText)
                .toList();

        return null;
    }

    @Override
    public TSLDeferred<TSLCapture> visitActionArgs(TSLParserImpl.ActionArgsContext ctx) {
        this.contents = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (child instanceof TSLParserImpl.WordContext wordChild) {
                TSLWord word = new TSLWordInterpreter().interpret(wordChild);
                this.contents.add(Either.left(word));

            } else if (child instanceof TSLParserImpl.ActionNestContext nestChild) {
                TSLParserImpl.ActionContext actionTree = nestChild.action();
                TSLDeferred<TSLAction> actionRef = new TSLActionInterpreter().interpret(actionTree);
                this.contents.add(Either.right(actionRef));
            }
        }

        return null;
    }

}
