package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.defer.TSLActionRef;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class TSLActionInterpreter extends TSLInterpreter<TSLActionRef, TSLParserImpl.ActionContext> {

    protected String name;
    protected List<Either<TSLWord, TSLActionRef>> args;
    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLWord displaying;

    @Override
    public TSLActionRef yieldValue(TSLParserImpl.ActionContext tree) {
        return new TSLActionRef(name, args, yieldConsumer, displaying);
    }

    @Override
    public TSLActionRef visitActionId(TSLParserImpl.ActionIdContext ctx) {
        this.name = ctx.IDENTIFIER().getText();

        return null;
    }

    @Override
    public TSLActionRef visitActionArgs(TSLParserImpl.ActionArgsContext ctx) {
        this.args = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (child instanceof TSLParserImpl.WordContext wordChild) {
                TSLWord word = new TSLWordInterpreter().interpret(wordChild);
                this.args.add(Either.left(word));

            } else if (child instanceof TSLParserImpl.ActionNestContext nestChild) {
                TSLParserImpl.ActionContext actionTree = nestChild.action();
                TSLActionRef actionRef = new TSLActionInterpreter().interpret(actionTree);
                this.args.add(Either.right(actionRef));
            }
        }

        return null;
    }

    @Override
    public TSLActionRef visitActionYields(TSLParserImpl.ActionYieldsContext ctx) {
        TSLWord yieldConsumer = new TSLWordInterpreter().parseWord(ctx.consumer);

        if (yieldConsumer instanceof TSLCaptureId) {
            this.yieldConsumer = Either.left(((TSLCaptureId) yieldConsumer));
        } else if (yieldConsumer instanceof TSLExpression) {
            this.yieldConsumer = Either.right(((TSLExpression) yieldConsumer));
        }

        return null;
    }

    @Override
    public TSLActionRef visitActionDisplaying(TSLParserImpl.ActionDisplayingContext ctx) {
        this.displaying = new TSLWordInterpreter().interpret(ctx.word());

        return null;
    }

}
