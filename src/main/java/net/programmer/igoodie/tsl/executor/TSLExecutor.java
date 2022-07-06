package net.programmer.igoodie.tsl.executor;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TSLExecutor {

    private final ThreadGroup threadGroup;
    private final ExecutorService executorService;

    public TSLExecutor(String threadGroupName) {
        this.threadGroup = new ThreadGroup(threadGroupName);
        this.executorService = Executors.newSingleThreadExecutor(runnable -> new Thread(threadGroup, runnable));
    }

    public void execute(TSLRuleset ruleset, TSLContext context) {
        executorService.execute(() -> ruleset.perform(context));
    }

}
