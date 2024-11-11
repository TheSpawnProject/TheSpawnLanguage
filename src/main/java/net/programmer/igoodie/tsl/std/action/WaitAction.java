package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaitAction extends TSLAction {

    private final long sleepTime;

    /*
     * WAIT <number> <time_unit>
     * 10 milliseconds
     * 10 seconds
     * 10 minutes
     */
    public WaitAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        if (args.size() != 2) {
            throw new TSLSyntaxException("Expected two words, found %d instead", args.size());
        }

        try {
            TimeUnit timeUnit = TimeUnit.valueOf(args.get(1).toUpperCase());
            this.sleepTime = timeUnit.toMillis(parseInt(args.get(0)));

        } catch (IllegalArgumentException e) {
            throw new TSLSyntaxException("Unexpected time unit -> %s", args.get(1));
        }
    }

    @Override
    public boolean perform(TSLEventContext ctx) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
