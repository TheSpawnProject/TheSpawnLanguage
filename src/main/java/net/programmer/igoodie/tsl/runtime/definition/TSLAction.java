package net.programmer.igoodie.tsl.runtime.definition;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLException;
import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
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

    /* ----------------------- */

    public static class Deferred extends TSLAction implements TSLDeferred<TSLAction> {

        protected final String name;

        public Deferred(String name, List<TSLClause> sourceArguments) throws TSLSyntaxException {
            super(sourceArguments);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public Deferred setYieldConsumer(Either<TSLCaptureId, TSLExpression> yieldConsumer) {
            return (Deferred) super.setYieldConsumer(yieldConsumer);
        }

        @Override
        public Deferred setDisplaying(TSLToken displaying) {
            return (Deferred) super.setDisplaying(displaying);
        }

        @Override
        public TSLAction resolve(TSLPlatform platform) throws TSLException {
            TSLAction.Supplier<?> supplier = platform.getActionDefinition(this.name)
                    .orElseThrow(() -> new TSLInternalException("Unresolvable action -> {}", this.name));

            return supplier.createAction(this.sourceArguments)
                    .setYieldConsumer(this.yieldConsumer)
                    .setDisplaying(this.displaying);
        }

        @Override
        public void parseArguments(TSLPlatform platform, List<TSLClause> arguments) throws TSLSyntaxException {
            throw new IllegalStateException("Called parseArguments on a deferred action holder");
        }

        @Override
        public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
            throw new IllegalStateException("Called perform on a deferred action holder");
        }

    }

}
