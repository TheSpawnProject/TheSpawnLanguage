package net.programmer.igoodie.tsl.context;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.mozilla.javascript.ScriptableObject;

public class TSLContext {

    protected TheSpawnLanguage tsl;

    public TSLContext(TheSpawnLanguage tsl) {
        this.tsl = tsl;
    }

    public TheSpawnLanguage getTsl() {
        return tsl;
    }

    /* ----------------------------------- */

    protected ScriptableObject ruleScope;

    public ScriptableObject getRuleScope() {
        return ruleScope;
    }

    public void setRuleScope(ScriptableObject ruleScope) {
        this.ruleScope = ruleScope;
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

    /* ----------------------------------- */

    protected TSLToken messageToken;

    public TSLToken getMessageToken() {
        return messageToken;
    }

    public void setMessageToken(TSLToken messageToken) {
        this.messageToken = messageToken;
    }

}
