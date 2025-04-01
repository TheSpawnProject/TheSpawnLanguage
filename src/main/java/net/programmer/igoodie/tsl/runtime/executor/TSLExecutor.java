package net.programmer.igoodie.tsl.runtime.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

public class TSLExecutor {

    protected final String target;
    protected TaskThread thread;

    public TSLExecutor(String target) {
        this.target = target;
        this.thread = new TaskThread();
        this.thread.setName("TSLExecutor:" + target);
    }

    public void start() {
        this.thread.isRunning = true;
        this.thread.start();
    }

    public void stop() {
        this.thread.isRunning = false;
        this.thread.interrupt();
    }

    public CompletableFuture<Void> queueTask(TaskThread.Task.Logic task) {
        synchronized (this.thread.tasks) {
            TaskThread.Task threadTask = new TaskThread.Task();
            threadTask.logic = task;
            threadTask.future = new CompletableFuture<>();

            this.thread.tasks.offer(threadTask);
            this.thread.tasks.notifyAll();

            return threadTask.future;
        }
    }

    public void subscribeTaskFailListener(BiConsumer<TaskThread.Task.Logic, Exception> listener) {
        this.thread.taskFailListeners.add(listener);
    }

    public static class TaskThread extends Thread {

        protected boolean isRunning;

        public static class Task {
            protected CompletableFuture<Void> future;
            protected Logic logic;

            public interface Logic {
                void run() throws Exception;
            }
        }

        protected final Queue<Task> tasks = new ConcurrentLinkedQueue<>();
        protected final List<BiConsumer<Task.Logic, Exception>> taskFailListeners = new ArrayList<>();

        @Override
        public void run() {
            while (this.isRunning) {
                synchronized (this.tasks) {
                    try {
                        while (this.tasks.isEmpty()) this.tasks.wait();
                    } catch (InterruptedException ignored) {
                        // Ignored, we can discard tasks, if the executor is stopped/interrupted
                    }

                    Task task = tasks.poll();

                    if (task != null) {
                        try {
                            task.logic.run();
                            task.future.complete(null);
                        } catch (Exception e) {
                            task.future.completeExceptionally(e);
                            taskFailListeners.forEach(listener -> listener.accept(task.logic, e));
                        }
                    }
                }
            }
        }

    }

//    public static void main(String[] args) {
//        String[] targets = {"Dummy1", "Dummy2", "Dummy3"};
//
//        for (String target : targets) {
//            TSLExecutor executor = new TSLExecutor(target);
//
//            executor.subscribeTaskFailListener((task, e) -> {
//                System.out.println("Task failed:" + e.getMessage());
//                System.out.println("Queueing again");
//                executor.queueTask(task);
//            });
//
//            executor.queueTask(() -> System.out.println(System.currentTimeMillis() + " Hi, " + target));
//            executor.queueTask(() -> Thread.sleep(2_000));
//            executor.queueTask(() -> System.out.println(System.currentTimeMillis() + " Hello, " + target));
//            executor.queueTask(() -> Thread.sleep(5_000));
//            executor.queueTask(executor::stop).thenRunAsync(() -> System.out.println("Executor stopped"));
//
//            // Should be discarded
//            executor.queueTask(() -> Thread.sleep(10_000));
//            executor.queueTask(() -> System.out.println(System.currentTimeMillis() + " Hello2"));
//            executor.queueTask(() -> {throw new Exception("Wops");});
//
//            System.out.println("Procedure queued for " + target);
//
//            executor.start();
//
//            System.out.println("Executor started for " + target);
//
//            executor.queueTask(() -> System.out.println("Not crashed, even after task exception!"));
//        }
//    }

}
