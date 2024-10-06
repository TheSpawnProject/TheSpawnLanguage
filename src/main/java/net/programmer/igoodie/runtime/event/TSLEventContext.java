package net.programmer.igoodie.runtime.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class TSLEventContext {

    protected String eventName;
    protected String target;
    protected GoodieObject eventArgs;

    public TSLEventContext(String eventName) {
        this.eventName = eventName;
        this.eventArgs = new GoodieObject();
    }

    public String getEventName() {
        return eventName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public GoodieObject getEventArgs() {
        return eventArgs;
    }

}
