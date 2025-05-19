package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TSLCaptureInterpreter extends TSLInterpreter<TSLDeferred<TSLCapture>, TSLParserImpl.CaptureRuleContext> {

    protected TSLCaptureId id;
    protected List<String> params;
    protected List<Either<TSLWord, TSLDeferred<TSLAction>>> contents;

    @Override
    public TSLDeferred<TSLCapture> yieldValue(TSLParserImpl.CaptureRuleContext tree) {
        return platform -> {
            try {
                List<Either<TSLWord, TSLAction>> resolvedContent = this.contents.stream()
                        .map(argRef -> argRef.map(
                                word -> word,
                                nestRef -> nestRef.resolve(platform).orElseThrow()
                        ))
                        .toList();

                return Optional.of(new TSLCapture(this.id, this.params, resolvedContent));

            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    @Override
    public TSLDeferred<TSLCapture> visitCaptureHeader(TSLParserImpl.CaptureHeaderContext ctx) {
        this.id = (TSLCaptureId) new TSLWordInterpreter().parseWord(ctx.id);

        this.params = ctx.captureParams().IDENTIFIER().stream()
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
