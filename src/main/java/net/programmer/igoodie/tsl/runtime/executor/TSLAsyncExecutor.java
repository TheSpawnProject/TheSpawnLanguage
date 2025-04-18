package net.programmer.igoodie.tsl.runtime.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TSLAsyncExecutor implements Executor {

    protected final String target;
    protected final ThreadGroup threadGroup;

    public TSLAsyncExecutor(String target) {
        this.target = target;
        this.threadGroup = new ThreadGroup("TSLExecutor-" + target);
    }

    @Override
    public void execute(Runnable command) {
        new Thread(threadGroup, command, "Executor-\"" + target + "\"").start();
    }

    public <V> CompletableFuture<V> executeTaskAsync(Callable<V> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, this);
    }

    public <V> CompletableFuture<List<V>> executeProcedureAsync(Callable<V>... tasks) {
        return this.executeProcedureAsync(new Procedure<>(tasks));
    }

    public <V> CompletableFuture<List<V>> executeProcedureAsync(Procedure<V> procedure) {
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
