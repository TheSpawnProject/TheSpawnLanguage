package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLActionNest;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class TSLActionInterpreter extends TSLInterpreter<TSLAction.Deferred, TSLParserImpl.ActionContext> {

    protected String name;
    protected List<TSLClause> args;
    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLToken displaying;

    @Override
    protected TSLAction.Deferred yieldValue(TSLParserImpl.ActionContext tree) {
        return new TSLAction.Deferred(this.name, this.args);
    }

    @Override
    public TSLAction.Deferred visitActionId(TSLParserImpl.ActionIdContext ctx) {
        this.name = ctx.IDENTIFIER().getText();

        return null;
    }

    @Override
    public TSLAction.Deferred visitActionArgs(TSLParserImpl.ActionArgsContext ctx) {
        this.args = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (child instanceof TSLParserImpl.WordContext wordChild) {
                TSLToken token = new TSLTokenInterpreter().interpret(wordChild);
                this.args.add(token);

            } else if (child instanceof TSLParserImpl.WordNestContext nestChild) {
                TSLActionNest actionNest = new TSLActionNestInterpreter().interpret(nestChild.wordNestContent());
                this.args.add(actionNest);
            }
        }

        return null;
    }

    @Override
    public TSLAction.Deferred visitActionYielding(TSLParserImpl.ActionYieldingContext ctx) {
        TSLToken yieldConsumer = new TSLTokenInterpreter().interpretToken(ctx.consumer);

        if (yieldConsumer instanceof TSLCaptureId captureConsumer) {
            this.yieldConsumer = Either.left(captureConsumer);
        } else if (yieldConsumer instanceof TSLExpression expressionConsumer) {
            this.yieldConsumer = Either.right(expressionConsumer);
        }

        return null;
    }

    @Override
    public TSLAction.Deferred visitActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx) {
        this.displaying = new TSLTokenInterpreter().interpret(ctx.word());

        return null;
    }

}
