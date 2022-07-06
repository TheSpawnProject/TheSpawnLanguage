package net.programmer.igoodie.tsl.executor;

import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TSLExecutor {

    private final ThreadGroup threadGroup;
    private final ExecutorService executorService;

    public TSLExecutor(String threadGroupName) {
        this.threadGroup = new ThreadGroup(threadGroupName);
        this.executorService = Executors.newSingleThreadExecutor(runnable -> new Thread(threadGroup, runnable));
    }

    public void execute(TSLRulesetOld ruleset, TSLContext context) {
        executorService.execute(() -> ruleset.perform(context));
    }

}
