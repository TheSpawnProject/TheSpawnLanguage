package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.parser.snippet.TSLRuleSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
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

    public TSLRule() {
        this.attributeList = new TSLAttributeList();
    }

    public TSLRule(TSLRuleset ruleset) {
        this();
        this.ruleset = ruleset;
    }

    public TSLRule(TSLRuleset ruleset, TSLRuleSnippet snippet, TSLEvent event) {
        this(ruleset);
        this.snippet = snippet;
        this.event = event;
    }

    public TSLAttributeList getAttributeList() {
        return attributeList;
    }

    public TSLRuleset getRuleset() {
        return ruleset;
    }

    public void setRuleset(TSLRuleset ruleset) {
        if (this.ruleset != null)
            throw new IllegalStateException("Ruleset MUST not be re-initialized");
        this.ruleset = ruleset;
    }

    public TSLRuleSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(TSLRuleSnippet snippet) {
        if (this.snippet != null)
            throw new IllegalStateException("Snippet MUST not be re-initialized");
        this.snippet = snippet;
    }

    /* ----------------------------------- */

    @Override
    public JsonObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    public JsonObject getCalculatedAttributes() {
        return GsonUtils.mergeOverriding(ruleset.getAttributes(), this.getAttributes());
    }

    /* ----------------------------------- */

    public void addDecorator(TSLDecorator decoratorDefinition, TSLDecoratorCall decoratorCall) {
        this.attributeList.addDecorator(decoratorDefinition, decoratorCall);
    }

    // TODO: Handler thingy

}
