package net.programmer.igoodie.tsl.eventqueue;

import java.util.Deque;
import java.util.LinkedList;

public class TSLEventQueue {

    protected final Thread executorThread;
    protected volatile State state;
    protected final Deque<EventQueueTask> tasks;

    public TSLEventQueue(String queueName) {
        this.executorThread = new Thread(() -> {
            while (!Thread.interrupted()) stepThread();
        }, queueName);
        this.state = State.PAUSED;
        this.tasks = new LinkedList<>();

        this.executorThread.start();
    }

    public State getState() {
        synchronized (executorThread) {
            return state;
        }
    }

    protected void stepThread() {
        if (tasks.size() == 0) {
            pause();
            return;
        }

        state = State.EXECUTING;

        EventQueueTask task = tasks.removeFirst();

        try {
            task.run();
            onTaskSucceed(task);
        } catch (Throwable e) {
            onTaskFailed(task);
        }

        state = State.IDLE;
    }

    protected void unpause() {
        synchronized (executorThread) {
            this.state = State.IDLE;
            this.executorThread.notifyAll();
        }
    }

    protected void pause() {
        synchronized (executorThread) {
            try {
                this.state = State.PAUSED;
                this.executorThread.wait();
            } catch (InterruptedException e) {
                throw new InternalError(e);
            }
        }
    }

    public void kill() {
        executorThread.interrupt();
    }

    /* ---------------------------- */

    public void queueTask(Runnable task) {
        synchronized (executorThread) {
            queueTask(new EventQueueTask() {
                @Override
                public void run() {
                    task.run();
                }
            });
        }
    }

    public void queueTask(EventQueueTask task) {
        synchronized (executorThread) {
            this.tasks.add(task);
            unpause();
        }
    }

    public void onTaskSucceed(EventQueueTask task) {}

    public void onTaskFailed(EventQueueTask task) {}

    enum State {
        IDLE,
        EXECUTING,
        PAUSED
    }

}
