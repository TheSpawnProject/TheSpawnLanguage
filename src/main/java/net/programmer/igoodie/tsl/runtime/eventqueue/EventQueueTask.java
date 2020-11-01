package net.programmer.igoodie.tsl.runtime.eventqueue;

import java.util.Timer;
import java.util.TimerTask;

public class EventQueueTask {

    final String name;
    Runnable taskRoutine;
    long cooldown;

    public EventQueueTask(Runnable taskRoutine) {
        this("Executable Task", taskRoutine);
    }

    public EventQueueTask(String name, Runnable taskRoutine) {
        this.name = name;
        this.taskRoutine = taskRoutine;
    }

    public EventQueueTask(long cooldown) {
        this("Cooldown Task", cooldown);
    }

    public EventQueueTask(String name, long cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    private void sleep(long millis) throws InterruptedException {
        final Thread sleepingThread = Thread.currentThread();
        synchronized (sleepingThread) {
            // Set timer to notify this sleeping thread
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (sleepingThread) {
                        sleepingThread.notifyAll();
                    }
                }
            }, cooldown);

            // Put this thread to sleep
            sleepingThread.wait();
        }
    }

    public void run() throws InterruptedException {
        switch (getType()) {
            case ROUTINE:
                taskRoutine.run();
                break;

            case SLEEP:
                sleep(cooldown);
        }
    }

    public Type getType() {
        if (taskRoutine != null)
            return Type.ROUTINE;
        return Type.SLEEP;
    }

    @Override
    public String toString() {
        return name + (cooldown == 0 ? "" : String.format("(%d)", cooldown));
    }

    /* --------------------------------- */

    public enum Type {
        SLEEP, ROUTINE
    }

}
