package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

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

    protected TSLToken unitWord;
    protected TSLToken sleepTimeWord;

    public WaitAction(List<TSLClause> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void parseArguments(TSLPlatform platform, List<TSLClause> arguments) throws TSLSyntaxException {
        if (arguments.size() != 2) {
            throw new TSLSyntaxException("Expected two words, found %d instead", this.sourceArguments.size());
        }

        this.sleepTimeWord = arguments.get(0).expectToken();
        this.unitWord = arguments.get(1).expectToken();
    }

    @Override
    public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
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
