package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaitAction extends TSLAction {

    protected final TSLWord unitWord;
    protected final TSLWord sleepTimeWord;

    /*
     * WAIT <number> <time_unit>
     * 10 milliseconds
     * 10 seconds
     * 10 minutes
     */
    public WaitAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException {
        super(platform, args);

        if (args.size() != 2) {
            throw new TSLSyntaxException("Expected two words, found %d instead", args.size());
        }

        this.sleepTimeWord = args.get(0).getLeftOrThrow(
                () -> new TSLSyntaxException("Expected a word")
        );

        this.unitWord = args.get(1).getLeftOrThrow(
                () -> new TSLSyntaxException("Expected a word")
        );
    }

    @Override
    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        try {
            TimeUnit timeUnit = TimeUnit.valueOf(this.unitWord.evaluate(ctx).toUpperCase());
            long sleepTime = timeUnit.toMillis(Integer.parseInt(this.sleepTimeWord.evaluate(ctx)));
            Thread.sleep(sleepTime);

            // Does not yield anything
            return Collections.emptyList();

        } catch (IllegalArgumentException e) {
            throw new TSLPerformingException("Unexpected time unit -> {}", this.unitWord);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
