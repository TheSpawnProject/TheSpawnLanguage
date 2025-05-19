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
import java.util.Optional;

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

    public static class Ref {

        protected final String name;
        protected final List<Either<TSLWord, TSLAction.Ref>> args;
        protected final Either<TSLCaptureId, TSLExpression> yieldConsumer;
        protected final TSLWord displaying;

        public Ref(String name, List<Either<TSLWord, TSLAction.Ref>> args, Either<TSLCaptureId, TSLExpression> yieldConsumer, TSLWord displaying) {
            this.name = name;
            this.args = args;
            this.yieldConsumer = yieldConsumer;
            this.displaying = displaying;
        }

        public Optional<TSLAction> resolve(TSLPlatform platform) {
            try {
                List<Either<TSLWord, TSLAction>> resolvedArgs = this.args.stream()
                        .map(argRef -> argRef.map(
                                word -> word,
                                nestRef -> nestRef.resolve(platform).orElseThrow()
                        ))
                        .toList();

                TSLAction.Supplier<?> supplier = platform.getActionDefinition(this.name).orElseThrow();

                return Optional.ofNullable(supplier.createAction(platform, resolvedArgs));

            } catch (Exception e) {
                return Optional.empty();
            }
        }

    }


}
