package net.programmer.igoodie.runtime.event;

import net.programmer.igoodie.TSLPlatform;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class TSLEventContext {

    protected TSLPlatform platform;
    protected String eventName;
    protected String target;
    protected GoodieObject eventArgs;

    public TSLEventContext(TSLPlatform platform, String eventName) {
        this.platform = platform;
        this.eventName = eventName;
        this.eventArgs = new GoodieObject();
    }

    public TSLPlatform getPlatform() {
        return platform;
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
