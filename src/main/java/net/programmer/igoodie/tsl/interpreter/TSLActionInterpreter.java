package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLTokenNest;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class TSLActionInterpreter extends TSLInterpreter<TSLDeferred<TSLAction>, TSLParserImpl.ActionContext> {

    protected String name;
    protected List<TSLClause> args;
    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLToken displaying;

    @Override
    protected TSLDeferred<TSLAction> yieldValue(TSLParserImpl.ActionContext tree) {
        return platform -> {
            TSLAction.Supplier<?> supplier = platform.getActionDefinition(this.name)
                    .orElseThrow(() -> new TSLInternalException("Unresolvable action -> {}", this.name));

            return supplier.createAction(this.args)
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
                TSLToken token = new TSLTokenInterpreter().interpret(wordChild);
                this.args.add(token);

            } else if (child instanceof TSLParserImpl.WordNestContext nestChild) {
                TSLTokenNest tokenNest = new TSLTokenNestInterpreter().interpret(nestChild.wordNestContent());
                this.args.add(tokenNest);
            }
        }

        return null;
    }

    @Override
    public TSLDeferred<TSLAction> visitActionYielding(TSLParserImpl.ActionYieldingContext ctx) {
        TSLToken yieldConsumer = new TSLTokenInterpreter().interpretToken(ctx.consumer);

        if (yieldConsumer instanceof TSLCaptureId captureConsumer) {
            this.yieldConsumer = Either.left(captureConsumer);
        } else if (yieldConsumer instanceof TSLExpression expressionConsumer) {
            this.yieldConsumer = Either.right(expressionConsumer);
        }

        return null;
    }

    @Override
    public TSLDeferred<TSLAction> visitActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx) {
        this.displaying = new TSLTokenInterpreter().interpret(ctx.word());

        return null;
    }

}
