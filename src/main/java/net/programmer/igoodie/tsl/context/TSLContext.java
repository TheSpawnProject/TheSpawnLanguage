package net.programmer.igoodie.tsl.context;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.mozilla.javascript.ScriptableObject;

public class TSLContext {

    protected TheSpawnLanguage language;

    public TSLContext(TheSpawnLanguage language) {
        this.language = language;
    }

    public TheSpawnLanguage getLanguage() {
        return language;
    }

    /* ----------------------------------- */

    protected ScriptableObject scope;

    public ScriptableObject getScope() {
        return scope;
    }

    public void setScope(ScriptableObject scope) {
        this.scope = scope;
    }

    /* ----------------------------------- */

    protected TSLEvent event;

    public TSLEvent getEvent() {
        return event;
    }

    public void setEvent(TSLEvent event) {
        this.event = event;
    }

    /* ----------------------------------- */

    protected GoodieObject eventArguments = new GoodieObject();

    public GoodieObject getEventArguments() {
        return eventArguments;
    }

    public void setEventArguments(GoodieObject eventArguments) {
        this.eventArguments = eventArguments;
    }

    /* ----------------------------------- */

    protected GoodieObject attributes;

    public GoodieObject getAttributes() {
        return attributes;
    }

    public void setAttributes(GoodieObject attributes) {
        this.attributes = attributes;
    }

    /* ----------------------------------- */

    @Deprecated
    protected TSLRule rule;

    @Deprecated
    public TSLRule getRule() {
        return rule;
    }

    @Deprecated
    public void setRule(TSLRule rule) {
        this.rule = rule;
    }

}
