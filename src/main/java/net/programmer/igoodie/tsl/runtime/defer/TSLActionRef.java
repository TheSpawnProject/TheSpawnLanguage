package net.programmer.igoodie.tsl.runtime.defer;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;
import java.util.Optional;

public class TSLActionRef {

    protected final String name;
    protected final List<Either<TSLWord, TSLActionRef>> args;

    public TSLActionRef(String name, List<Either<TSLWord, TSLActionRef>> args) {
        this.name = name;
        this.args = args;
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
