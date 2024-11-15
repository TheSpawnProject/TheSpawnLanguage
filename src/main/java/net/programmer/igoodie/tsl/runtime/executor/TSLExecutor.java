package net.programmer.igoodie.tsl.runtime.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TSLExecutor implements Executor {

    protected final String target;
    protected final ThreadGroup threadGroup;

    public TSLExecutor(String target) {
        this.target = target;
        this.threadGroup = new ThreadGroup("TSLExecutor-" + target);
    }

    @Override
    public void execute(Runnable command) {
        new Thread(threadGroup, command, "Executor-\"" + target + "\"").start();
    }

    public <V> CompletableFuture<V> resolveCallable(Callable<V> callable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, this);
    }

    public <V> CompletableFuture<List<V>> resolveProcedure(Procedure<V> procedure) {
        return CompletableFuture.supplyAsync(() -> {
            List<V> results = new ArrayList<>();

            for (Callable<V> command : procedure.steps) {
                try {
                    results.add(command.call());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return results;
        }, this);
    }

    public static class Procedure<V> {

        protected final List<Callable<V>> steps;

        @SafeVarargs
        public Procedure(Callable<V>... steps) {
            this(Arrays.asList(steps));
        }

        public Procedure(List<Callable<V>> steps) {
            this.steps = steps;
        }

    }

}
