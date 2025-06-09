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

/*
 * WAIT <number> <time_unit>
 * 10 milliseconds
 * 10 seconds
 * 10 minutes
 */
public class WaitAction extends TSLAction {

    protected TSLWord unitWord;
    protected TSLWord sleepTimeWord;

    public WaitAction(List<Either<TSLWord, TSLAction>> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void interpretArguments(TSLPlatform platform, List<Either<TSLWord, TSLAction>> words) throws TSLSyntaxException {
        if (words.size() != 2) {
            throw new TSLSyntaxException("Expected two words, found %d instead", words.size());
        }

        this.sleepTimeWord = words.get(0).getLeft().orElseThrow(
                () -> new TSLSyntaxException("Expected a word")
        );

        this.unitWord = words.get(1).getLeft().orElseThrow(
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
