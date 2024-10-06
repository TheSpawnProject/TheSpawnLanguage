package net.programmer.igoodie.runtime.event;

import net.programmer.igoodie.TSL;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class TSLEventContext {

    protected TSL tsl;
    protected String eventName;
    protected String target;
    protected GoodieObject eventArgs;

    public TSLEventContext(TSL tsl, String eventName) {
        this.tsl = tsl;
        this.eventName = eventName;
        this.eventArgs = new GoodieObject();
    }

    public TSL getTsl() {
        return tsl;
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
