package net.programmer.igoodie.tsl.context;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.runtime.TSLRule;

public class TSLContext {

    protected JSEngine engine;

    public JSEngine getEngine() {
        return engine;
    }

    public void setEngine(JSEngine engine) {
        this.engine = engine;
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

//    protected List<TSLToken> actionTokens = new LinkedList<>();
//
//    public List<TSLToken> getActionTokens() {
//        return actionTokens;
//    }
//
//    public void setActionTokens(List<TSLToken> actionTokens) {
//        this.actionTokens = actionTokens;
//    }

    /* ----------------------------------- */

    protected TSLRule rule;

    public TSLRule getRule() {
        return rule;
    }

    public void setRule(TSLRule rule) {
        this.rule = rule;
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

//    protected Map<String, Object> customDataMap;
//
//    public Map<String, Object> getCustomDataMap() {
//        return customDataMap == null ? new HashMap<>() : customDataMap;
//    }
//
//    public Object getCustomData(String dataName) {
//        return getCustomDataMap().get(dataName);
//    }
//
//    public <T> T getCustomData(String dataName, Class<T> type) {
//        Map<String, Object> customDataMap = getCustomDataMap();
//        Object rawData = customDataMap.get(dataName);
//
//        if (rawData == null) return null;
//
//        if (type.isInstance(rawData)) {
//            return type.cast(rawData);
//        }
//
//        return null;
//    }

}
