package net.programmer.igoodie.tsl.runtime.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TSLPlatform;

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
