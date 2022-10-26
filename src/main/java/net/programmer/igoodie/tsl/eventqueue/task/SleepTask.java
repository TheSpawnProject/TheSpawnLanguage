package net.programmer.igoodie.tsl.eventqueue.task;

import net.programmer.igoodie.tsl.eventqueue.EventQueueTask;

public class SleepTask extends EventQueueTask {

    protected long millis;

    public SleepTask(long millis) {
        this.millis = millis;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            onInterrupted(e);
        }
    }

}
