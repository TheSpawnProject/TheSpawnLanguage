package net.programmer.igoodie.tsl.eventqueue;

import java.util.Deque;
import java.util.LinkedList;

public class TSLEventQueue {

    protected final Thread thread;
    protected volatile State state;
    protected final Deque<EventQueueTask> tasks;

    public TSLEventQueue(String queueName) {
        this.thread = new Thread(() -> {
            while (!Thread.interrupted()) stepThread();
        }, queueName);
        this.state = State.PAUSED;
        this.tasks = new LinkedList<>();

        this.thread.start();
    }

    public synchronized State getState() {
        return state;
    }

    protected void stepThread() {
        if (tasks.size() == 0) {
            pause();
            return;
        }

        state = State.EXECUTING;

        EventQueueTask task = tasks.removeFirst();
        task.run();

        state = State.IDLE;
    }

    protected void unpause() {
        synchronized (thread) {
            this.state = State.IDLE;
            this.thread.notifyAll();
        }
    }

    protected void pause() {
        synchronized (thread) {
            try {
                this.state = State.PAUSED;
                this.thread.wait();
            } catch (InterruptedException e) {
                throw new InternalError(e);
            }
        }
    }

    public void kill() {
        thread.interrupt();
    }

    /* ---------------------------- */

    public synchronized void queueEvent(Runnable task) {
        queueEvent(new EventQueueTask() {
            @Override
            public void run() {
                task.run();
            }
        });
    }

    public synchronized void queueEvent(EventQueueTask task) {
        this.tasks.add(task);
        unpause();
    }

    public void onTaskSucceed() {}

    public void onTaskFailed() {}

    enum State {
        IDLE,
        EXECUTING,
        PAUSED
    }

}
