package net.programmer.igoodie.tsl.runtime.defer;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;
import java.util.Optional;

public class TSLActionRef {

    protected final String name;
    protected final List<Either<TSLWord, TSLActionRef>> args;
    protected final Either<TSLCaptureId, TSLExpression> yieldConsumer;
    protected final TSLWord displaying;

    public TSLActionRef(String name, List<Either<TSLWord, TSLActionRef>> args, Either<TSLCaptureId, TSLExpression> yieldConsumer, TSLWord displaying) {
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
