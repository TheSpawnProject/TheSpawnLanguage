package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.action.OLD_TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.Utils;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/*
 * CONCURRENTLY <action>
 * [AND <action>]+
 */
public class ConcurrentlyAction extends SequentiallyAction {

    public ConcurrentlyAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException {
        super(platform, args);
    }

    @Override
    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        CompletableFuture<?>[] futures = new CompletableFuture[actions.size()];

        for (int i = 0; i < actions.size(); i++) {
            int actionIndex = i;
            TSLAction action = actions.get(i);

            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    List<TSLWord> yield = action.perform(ctx);
                    // TODO: Handle yield somehow
                    return yield;

                } catch (TSLPerformingException e) {
                    throw new CompletionException(e);
                }
            });
        }

        CompletableFuture.allOf(futures).join();

        return Collections.emptyList();
    }

}
