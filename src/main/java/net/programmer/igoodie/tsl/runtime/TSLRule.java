package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.node.EventNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TSLRule {

    protected TSLRuleset ruleset;

    protected List<TSLDecorator> decorators;
    protected Map<TSLDecorator, JsonObject> attributeMap;

    protected List<TSLToken> actionTokens;
    protected EventNode eventNode;

    public TSLRule(EventNode eventNode, List<TSLToken> actionTokens) {
        this.eventNode = eventNode;
        this.actionTokens = actionTokens;
        this.decorators = new LinkedList<>();
        this.attributeMap = new HashMap<>();
    }

    public TSLRuleset getRuleset() {
        return ruleset;
    }

    public void setRuleset(TSLRuleset ruleset) {
        this.ruleset = ruleset;
    }

    public EventNode getEventNode() {
        return eventNode;
    }

    public List<TSLDecorator> getDecorators() {
        return decorators;
    }

    public Map<TSLDecorator, JsonObject> getAttributeMap() {
        return attributeMap;
    }

    public JsonObject getSquashedAttributes() {
        JsonObject squashed = new JsonObject();

        for (TSLDecorator decorator : decorators) {
            TSLPlugin plugin = decorator.getPlugin();
            JsonObject attributes = attributeMap.get(decorator);
            for (String field : attributes.keySet()) {
                squashed.add(plugin.prependNamespace(field), attributes.get(field));
            }
        }

        return squashed;
    }

    public void addDecorator(TSLDecorator decorator, TSLString decoratorName, List<TSLString> args) {
        this.decorators.add(decorator);
        this.attributeMap.put(decorator, decorator.evaluateAttributes(decoratorName, args));
    }

    // TODO: Handler thingy

}
