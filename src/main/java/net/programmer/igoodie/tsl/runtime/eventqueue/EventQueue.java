package net.programmer.igoodie.tsl.runtime.eventqueue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class EventQueue {

    private volatile State state;

    private final Thread innerThread;

    private final Deque<EventQueueTask> tasks;

    private final long delayBetweenTasks; // milliseconds
    private int succeededTasks;
    private int discardedTasks;

    public EventQueue(String queueName, long delayBetweenTasks) {
        this.innerThread = new Thread(() -> {
            while (true) stepThread();
        }, queueName);

        this.delayBetweenTasks = delayBetweenTasks;
        this.state = State.PAUSED;
        this.tasks = new LinkedList<>();

        this.innerThread.start();
    }

    private void stepThread() {
        try {
            if (hasUnhandledTasks()) {
                synchronized (tasks) {
                    EventQueueTask task = tasks.remove();

                    if (task.getType() == EventQueueTask.Type.SLEEP)
                        state = State.COOLDOWN;

                    task.run();

                    if (task.getType() == EventQueueTask.Type.SLEEP)
                        state = State.RUNNING;
                }

            } else {
                pause();
            }
        } catch (Throwable error) {
            discardedTasks++;
            error.printStackTrace();
        }
    }

    private void unpause() {
        synchronized (innerThread) {
            state = State.RUNNING;
            innerThread.notifyAll();
        }
    }

    private void pause() {
        synchronized (innerThread) {
            try {
                state = State.PAUSED;
                innerThread.wait();

            } catch (InterruptedException e) {
                e.printStackTrace(); // TODO
            }
        }
    }

    public void cancelUpcomingSleep() {
        assert this.tasks instanceof LinkedList;
        List<EventQueueTask> tasks = (LinkedList<EventQueueTask>) this.tasks;
        for (int i = 0; i < tasks.size(); i++) {
            EventQueueTask task = tasks.get(i);
            if (task.getType() == EventQueueTask.Type.SLEEP) {
                tasks.remove(i);
                return;
            }
        }
    }

    /* --------------------------------- */

    public void queueSleepFirst() {
        queueSleepFirst(delayBetweenTasks);
    }

    public void queueSleepFirst(long millis) {
        tasks.addFirst(new EventQueueTask("Sleep", millis));
    }

    public void queueSleep() {
        queueSleep(delayBetweenTasks);
    }

    public void queueSleep(long millis) {
        tasks.add(new EventQueueTask("Sleep", millis));
    }

    public void queueFirst(String name, Runnable task) {
        tasks.addFirst(new EventQueueTask(name, task));
    }

    public void queue(Runnable task) {
        queue("Runnable task", task);
    }

    public void queue(String name, Runnable task) {
        tasks.add(new EventQueueTask(name, task));
    }


    public void updateThread() {
        if (state == State.PAUSED) unpause();
    }

    /* --------------------------------- */

    public synchronized int unhandledTaskCount() {
        return tasks.size();
    }

    public synchronized boolean hasUnhandledTasks() {
        return !tasks.isEmpty();
    }

    /* --------------------------------- */

    enum State {
        RUNNING,
        COOLDOWN,
        PAUSED
    }

}
