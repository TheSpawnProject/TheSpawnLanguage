package net.programmer.igoodie.tsl.runtime.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.OLD_TSLRule;

public class TSLEventContext {

    protected TSLPlatform platform;
    protected String eventName;
    protected String target;
    protected GoodieObject eventArgs;
    protected GoodieObject performState;
    protected OLD_TSLRule performingRule;

    public TSLEventContext(TSLPlatform platform, String eventName) {
        this.platform = platform;
        this.eventName = eventName;
        this.eventArgs = new GoodieObject();
        this.performState = new GoodieObject();
    }

    public TSLEventContext copy() {
        TSLEventContext ctx = new TSLEventContext(platform, eventName);
        ctx.target = this.target;
        ctx.eventArgs = this.eventArgs.deepCopy();
        ctx.performState = this.performState.deepCopy();
        ctx.performingRule = this.performingRule;
        return ctx;
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

    public GoodieObject getPerformState() {
        return performState;
    }

    public OLD_TSLRule getPerformingRule() {
        return performingRule;
    }

    public void setPerformingRule(OLD_TSLRule performingRule) {
        this.performingRule = performingRule;
    }

}
