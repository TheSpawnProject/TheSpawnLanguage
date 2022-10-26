package net.programmer.igoodie.tsl.eventqueue;

import org.jetbrains.annotations.Nullable;

public abstract class EventQueueTask {

    public abstract void run();

    public void onInterrupted(@Nullable InterruptedException exception) {}

}
