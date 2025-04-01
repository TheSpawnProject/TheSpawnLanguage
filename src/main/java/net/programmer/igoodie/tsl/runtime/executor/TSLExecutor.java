package net.programmer.igoodie.tsl.runtime.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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

    public void queueTask(TaskThread.Task command) {
        synchronized (this.thread.tasks) {
            this.thread.tasks.offer(command);
            this.thread.tasks.notifyAll();
        }
    }

    public void subscribeTaskFailListener(BiConsumer<TaskThread.Task, Exception> listener) {
        this.thread.taskFailListeners.add(listener);
    }

    public static class TaskThread extends Thread {

        protected boolean isRunning;

        @FunctionalInterface
        public interface Task {
            void run() throws Exception;
        }

        protected final Queue<Task> tasks = new ConcurrentLinkedQueue<>();
        protected final List<BiConsumer<Task, Exception>> taskFailListeners = new ArrayList<>();

        @Override
        public void run() {
            while (this.isRunning) {
                synchronized (this.tasks) {
                    try {
                        while (this.tasks.isEmpty()) this.tasks.wait();
                    } catch (IllegalMonitorStateException e) {
                        throw e; // <-- This should not happen, as TSLExecutor is the only consumer
                    } catch (InterruptedException ignored) {
                        // Ignored, we can discard tasks, if the executor is stopped/interrupted
                    }

                    Task task = tasks.poll();

                    if (task != null) {
                        try {
                            task.run();
                        } catch (Exception e) {
                            taskFailListeners.forEach(listener -> listener.accept(task, e));
                        }
                    }
                }
            }
        }

    }

//    public static void main(String[] args) {
//        TSLExecutor executor = new TSLExecutor("DummyTarget");
//
//        executor.subscribeTaskFailListener((task, e) -> {
//            System.out.println("Task failed:" + e.getMessage());
//            System.out.println("Queueing again");
//            executor.queueTask(task);
//        });
//
//        executor.queueTask(() -> System.out.println(System.currentTimeMillis() + " Hi"));
//        executor.queueTask(() -> Thread.sleep(2000));
//        executor.queueTask(() -> System.out.println(System.currentTimeMillis() + " Hello"));
//        executor.queueTask(() -> Thread.sleep(5000));
//        executor.queueTask(executor::stop);
//        executor.queueTask(() -> Thread.sleep(10000));
//        executor.queueTask(() -> System.out.println(System.currentTimeMillis() + " Hello2"));
//        executor.queueTask(() -> {throw new Exception("Wops");});
//
//        executor.start();
//
//        executor.queueTask(() -> System.out.println("Not crashed, even after task exception!"));
//
//        System.out.println("Procedure queued");
//    }

}
