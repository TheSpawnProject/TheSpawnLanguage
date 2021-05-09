package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.parser.snippet.TSLRuleSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.attribute.Attributable;
import net.programmer.igoodie.tsl.runtime.attribute.TSLAttributeList;
import net.programmer.igoodie.tsl.runtime.node_old.EventNode;
import net.programmer.igoodie.tsl.util.GsonUtils;

import java.util.LinkedList;
import java.util.List;

public class TSLRule implements Attributable {

    protected TSLRuleset ruleset;
    protected TSLAttributeList attributeList;

    protected TSLRuleSnippet snippet;

    protected TSLEvent event;
    // TODO: Predicates

    public TSLRule(TSLRuleset ruleset, TSLRuleSnippet snippet, TSLEvent event) {
        this.ruleset = ruleset;
        this.snippet = snippet;
        this.event = event;
    }

    //    public TSLRule(EventNode eventNode, List<TSLToken> actionTokens) {
//        this.eventNode = eventNode;
//        this.actionTokens = actionTokens;
//        this.decorators = new LinkedList<>();
//        this.attributeList = new TSLAttributeList();
//    }
//
    public TSLRuleset getRuleset() {
        return ruleset;
    }

    public void setRuleset(TSLRuleset ruleset) {
        this.ruleset = ruleset;
    }

    public TSLRuleSnippet getSnippet() {
        return snippet;
    }

    //
//    public EventNode getEventNode() {
//        return eventNode;
//    }
//
//    public List<TSLDecorator> getDecorators() {
//        return decorators;
//    }
//
//    public TSLAttributeList getAttributeList() {
//        return attributeList;
//    }
//
//    public List<TSLToken> getActionTokens() {
//        return actionTokens;
//    }
//
    @Override
    public JsonObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }
//
//    public JsonObject getCalculatedAttributes() {
//        return GsonUtils.mergeOverriding(ruleset.getAttributes(), this.getAttributes());
//    }
//
//    public void addDecorator(TSLDecorator decoratorDefinition, TSLString decoratorTag, List<TSLString> args) {
//        this.decorators.add(decoratorDefinition);
//        this.attributeList.addDecorator(decoratorDefinition, decoratorTag, args);
//    }

    // TODO: Handler thingy

}
