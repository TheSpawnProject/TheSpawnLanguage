package net.programmer.igoodie.tsl.runtime.definition;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;

public abstract class TSLAction {

    protected final List<TSLClause> sourceArguments;
    protected Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected TSLToken displaying;

    public TSLAction(List<TSLClause> sourceArguments) throws TSLSyntaxException {
        this.sourceArguments = sourceArguments;
    }

    public List<TSLClause> getSourceArguments() {
        return sourceArguments;
    }

    public TSLAction setYieldConsumer(Either<TSLCaptureId, TSLExpression> yieldConsumer) {
        this.yieldConsumer = yieldConsumer;
        return this;
    }

    public TSLAction setDisplaying(TSLToken displaying) {
        this.displaying = displaying;
        return this;
    }

    public Either<TSLCaptureId, TSLExpression> getYieldConsumer() {
        return yieldConsumer;
    }

    public TSLToken getDisplaying() {
        return displaying;
    }

    /* ----------------------- */

    public abstract void parseArguments(TSLPlatform platform, List<TSLClause> arguments) throws TSLSyntaxException;

    public abstract List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException;

    /* ----------------------- */

    public interface Supplier<T extends TSLAction> {
        T createAction(List<TSLClause> sourceArguments) throws TSLSyntaxException;
    }

}
