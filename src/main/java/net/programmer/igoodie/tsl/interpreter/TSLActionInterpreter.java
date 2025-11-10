package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class TSLActionInterpreter extends TSLInterpreter<TSLDeferred<TSLAction>, TSLParserImpl.ActionContext> {

    protected String name;
    protected List<Either<TSLWord, TSLDeferred<TSLAction>>> args;
    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLWord displaying;

    @Override
    public TSLDeferred<TSLAction> yieldValue(TSLParserImpl.ActionContext tree) {
        return platform -> {
            List<TSLClause> resolvedArgs = this.args.stream()
                    .map(arg -> arg.reduce(
                            word -> word,
                            deferredAction -> deferredAction.resolve(platform)
                    ))
                    .toList();

            TSLAction.Supplier<?> supplier = platform.getActionDefinition(this.name)
                    .orElseThrow(() -> new TSLInternalException("Unresolvable action -> {}", this.name));

            return supplier.createAction(resolvedArgs)
                    .setYieldConsumer(this.yieldConsumer)
                    .setDisplaying(this.displaying);
        };
    }

    @Override
    public TSLDeferred<TSLAction> visitActionId(TSLParserImpl.ActionIdContext ctx) {
        this.name = ctx.IDENTIFIER().getText();

        return null;
    }

    @Override
    public TSLDeferred<TSLAction> visitActionArgs(TSLParserImpl.ActionArgsContext ctx) {
        this.args = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (child instanceof TSLParserImpl.WordContext wordChild) {
                TSLWord word = new TSLWordInterpreter().interpret(wordChild);
                this.args.add(Either.left(word));

            } else if (child instanceof TSLParserImpl.ActionNestContext nestChild) {
                TSLParserImpl.ActionContext actionTree = nestChild.action();
                TSLDeferred<TSLAction> actionRef = new TSLActionInterpreter().interpret(actionTree);
                this.args.add(Either.right(actionRef));
            }
        }

        return null;
    }

    @Override
    public TSLDeferred<TSLAction> visitActionYielding(TSLParserImpl.ActionYieldingContext ctx) {
        TSLWord yieldConsumer = new TSLWordInterpreter().interpretWord(ctx.consumer);

        if (yieldConsumer instanceof TSLCaptureId) {
            this.yieldConsumer = Either.left(((TSLCaptureId) yieldConsumer));
        } else if (yieldConsumer instanceof TSLExpression) {
            this.yieldConsumer = Either.right(((TSLExpression) yieldConsumer));
        }

        return null;
    }

    @Override
    public TSLDeferred<TSLAction> visitActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx) {
        this.displaying = new TSLWordInterpreter().interpret(ctx.word());

        return null;
    }

}
