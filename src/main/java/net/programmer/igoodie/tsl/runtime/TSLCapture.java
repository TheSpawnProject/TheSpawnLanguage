package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.List;
import java.util.Optional;

public class TSLCapture {

    protected final TSLCaptureId id;
    protected final List<String> params;
    protected final List<Either<TSLWord, TSLAction>> contents;

    public TSLCapture(TSLCaptureId id, List<String> params, List<Either<TSLWord, TSLAction>> contents) {
        this.id = id;
        this.params = params;
        this.contents = contents;
    }

    /* ----------------- */

    public static class Ref {

        protected final TSLCaptureId id;
        protected final List<String> params;
        protected final List<Either<TSLWord, TSLAction.Ref>> contents;

        public Ref(TSLCaptureId id, List<String> params, List<Either<TSLWord, TSLAction.Ref>> contents) {
            this.id = id;
            this.params = params;
            this.contents = contents;
        }

        public Optional<TSLCapture> resolve(TSLPlatform platform) {
            try {
                List<Either<TSLWord, TSLAction>> resolvedContent = this.contents.stream()
                        .map(argRef -> argRef.map(
                                word -> word,
                                nestRef -> nestRef.resolve(platform).orElseThrow()
                        ))
                        .toList();

                return Optional.of(new TSLCapture(this.id, this.params, resolvedContent));

            } catch (Exception e) {
                return Optional.empty();
            }
        }

    }

}
