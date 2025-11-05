package net.programmer.igoodie.tsl.runtime.definition;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;

public abstract class TSLAction {

    protected final List<Either<TSLWord, TSLAction>> sourceArguments;
    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLWord displaying;

    public TSLAction(List<Either<TSLWord, TSLAction>> sourceArguments) throws TSLSyntaxException {
        this.sourceArguments = sourceArguments;
    }

    public List<Either<TSLWord, TSLAction>> getSourceArguments() {
        return sourceArguments;
    }

    public TSLAction setYieldConsumer(Either<TSLCaptureId, TSLExpression> yieldConsumer) {
        this.yieldConsumer = yieldConsumer;
        return this;
    }

    public TSLAction setDisplaying(TSLWord displaying) {
        this.displaying = displaying;
        return this;
    }

    public Either<TSLCaptureId, TSLExpression> getYieldConsumer() {
        return yieldConsumer;
    }

    public TSLWord getDisplaying() {
        return displaying;
    }

    public abstract void interpretArguments(TSLPlatform platform) throws TSLSyntaxException;

    public abstract List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException;

    /* ----------------------- */

    public interface Supplier<T extends TSLAction> {
        T createAction(List<Either<TSLWord, TSLAction>> sourceArguments) throws TSLSyntaxException;
    }

}
