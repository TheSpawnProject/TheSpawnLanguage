package net.programmer.igoodie.legacy.eventqueue;

import java.util.Timer;
import java.util.TimerTask;

@Deprecated
public class EventQueueTask {

    private String name;
    private Runnable taskRoutine;
    private long cooldown;

    public static EventQueueTask sleepTask(long cooldown) {
        return sleepTask("Cooldown Task", cooldown);
    }

    public static EventQueueTask sleepTask(String name, long cooldown) {
        EventQueueTask queueTask = new EventQueueTask();
        queueTask.name = name;
        queueTask.cooldown = cooldown;
        return queueTask;
    }

    public static EventQueueTask routineTask(Runnable taskRoutine) {
        return routineTask("Executable Task", taskRoutine);
    }

    public static EventQueueTask routineTask(String name, Runnable taskRoutine) {
        EventQueueTask queueTask = new EventQueueTask();
        queueTask.name = name;
        queueTask.taskRoutine = taskRoutine;
        return queueTask;
    }

    private EventQueueTask() {}

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
