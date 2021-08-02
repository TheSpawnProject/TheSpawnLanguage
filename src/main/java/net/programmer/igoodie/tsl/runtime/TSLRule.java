package net.programmer.igoodie.tsl.runtime;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.parser.snippet.TSLRuleSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.attribute.Attributable;
import net.programmer.igoodie.tsl.runtime.attribute.TSLAttributeList;
import net.programmer.igoodie.tsl.util.GsonUtils;

import java.util.List;

public class TSLRule implements Attributable {

    protected TSLRuleset associatedRuleset;
    protected TSLAttributeList attributeList;

    protected TSLRuleSnippet snippet;

    protected TSLEvent event;
    // TODO: Predicates
    protected TSLAction action;

    public TSLRule() {
        this.attributeList = new TSLAttributeList();
    }

    public TSLRule(TSLRuleset ruleset) {
        this();
        this.associatedRuleset = ruleset;
    }

    public TSLRule(TSLRuleset ruleset, TSLRuleSnippet snippet) {
        this(ruleset);
        this.setSnippet(snippet);
    }

    public TSLAttributeList getAttributeList() {
        return attributeList;
    }

    public TSLRuleset getAssociatedRuleset() {
        return associatedRuleset;
    }

    public void setAssociatedRuleset(TSLRuleset associatedRuleset) {
        if (this.associatedRuleset != null)
            throw new IllegalStateException("Ruleset MUST not be re-initialized");
        this.associatedRuleset = associatedRuleset;
    }

    public TSLRuleSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(TSLRuleSnippet snippet) {
        if (this.snippet != null)
            throw new IllegalStateException("Snippet MUST not be re-initialized");
        this.snippet = snippet;
        this.event = snippet.getEventSnippet().getEventDefinition();
        this.action = snippet.getActionSnippet().getActionDefinition();
    }

    /* ----------------------------------- */

    @Override
    public JsonObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    public JsonObject getCalculatedAttributes() {
        return GsonUtils.mergeOverriding(associatedRuleset.getAttributes(), this.getAttributes());
    }

    /* ----------------------------------- */

    public void addDecorator(TSLDecorator decoratorDefinition, TSLDecoratorCall decoratorCall) {
        this.attributeList.addDecorator(decoratorDefinition, decoratorCall);
    }

    public boolean perform(TSLContext context) {
        if (context.getEvent() != this.event) {
            return false;
        }

        System.out.println("Performing " + snippet.getAllTokens());

        // TODO: traverse predicates

        context.setAttributes(getCalculatedAttributes());

        List<TSLToken> actionArgs = snippet.getActionSnippet().getActionArgs();
        action.perform(actionArgs, context);
        return true;
    }

}
