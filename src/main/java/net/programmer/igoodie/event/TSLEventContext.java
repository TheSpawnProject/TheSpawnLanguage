package net.programmer.igoodie.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class TSLEventContext {

    protected String eventName;
    protected GoodieObject eventArgs;

    public TSLEventContext(String eventName) {
        this.eventName = eventName;
        this.eventArgs = new GoodieObject();
    }

    public String getEventName() {
        return eventName;
    }

    public GoodieObject getEventArgs() {
        return eventArgs;
    }

}
