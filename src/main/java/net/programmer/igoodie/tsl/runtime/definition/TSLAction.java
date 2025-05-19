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

    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLWord displaying;

    public TSLAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException {
    }

    public void setYieldConsumer(Either<TSLCaptureId, TSLExpression> yieldConsumer) {
        this.yieldConsumer = yieldConsumer;
    }

    public void setDisplaying(TSLWord displaying) {
        this.displaying = displaying;
    }

    public Either<TSLCaptureId, TSLExpression> getYieldConsumer() {
        return yieldConsumer;
    }

    public TSLWord getDisplaying() {
        return displaying;
    }

    public abstract List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException;

    /* ----------------------- */

    public interface Supplier<T extends TSLAction> {
        T createAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException;
    }

}
