package net.programmer.igoodie.tsl.runtime.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.Optional;

public class TSLEventContext {

    protected final TSLPlatform platform;
    protected final String eventName;
    protected GoodieObject eventArgs;
    protected GoodieObject performState;

    protected String target;
    protected TSLRule performingRule;
    protected TSLRuleset performingRuleset;

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
        return this.platform;
    }

    public String getEventName() {
        return this.eventName;
    }

    public Optional<String> getTarget() {
        return Optional.ofNullable(this.target);
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

    public Optional<TSLRule> getPerformingRule() {
        return Optional.ofNullable(performingRule);
    }

    public void setPerformingRule(TSLRule performingRule) {
        this.performingRule = performingRule;
    }

    public Optional<TSLRuleset> getPerformingRuleset() {
        return Optional.ofNullable(performingRuleset);
    }

    public void setPerformingRuleset(TSLRuleset performingRuleset) {
        this.performingRuleset = performingRuleset;
    }

}
