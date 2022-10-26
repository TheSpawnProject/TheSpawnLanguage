package net.programmer.igoodie.tsl.eventqueue.task;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.eventqueue.EventQueueTask;

public class EventDispatchTask extends EventQueueTask {

    protected TSLEvent event;
    protected GoodieObject eventArguments;

    @Override
    public void run() {
        // TODO
    }

}
