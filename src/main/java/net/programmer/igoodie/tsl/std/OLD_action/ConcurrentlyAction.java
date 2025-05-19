package net.programmer.igoodie.tsl.std.OLD_action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.OLD_TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

// CONCURRENTLY <action> [AND <action>]+ [DISPLAYING msg+]?
public class ConcurrentlyAction extends OLD_TSLAction {

    protected List<OLD_TSLAction> actions;

    public ConcurrentlyAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        this.actions = new ArrayList<>();

        List<List<String>> actionChunks = Utils.splitIntoChunks(args, arg -> arg.equalsIgnoreCase("AND"));

        for (List<String> actionChunk : actionChunks) {
            if (actionChunk.isEmpty()) {
                throw new TSLSyntaxException("");
            }

//            TSLAction action = TSLParser.immediate(platform, actionChunk).parseAction();
//            this.actions.add(action);
        }
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        boolean[] successes = new boolean[actions.size()];
        CompletableFuture<?>[] futures = new CompletableFuture[actions.size()];

        for (int i = 0; i < actions.size(); i++) {
            int actionIndex = i;
            OLD_TSLAction action = actions.get(i);

            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    boolean success = action.perform(ctx);
                    successes[actionIndex] = success;
                    return success;

                } catch (TSLPerformingException e) {
                    throw new CompletionException(e);
                }
            });
        }

        CompletableFuture.allOf(futures).join();

        for (boolean success : successes) {
            if (!success) return false;
        }

        return true;
    }

}
