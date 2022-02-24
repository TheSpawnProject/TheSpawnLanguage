package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.parser.snippet.TSLPredicateSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLRuleSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.attribute.Attributable;
import net.programmer.igoodie.tsl.runtime.attribute.TSLAttributeList;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.mozilla.javascript.ScriptableObject;

import java.util.List;
import java.util.stream.Collectors;

public class TSLRule implements Attributable {

    protected TSLRuleset associatedRuleset;
    protected TSLAttributeList attributeList;

    protected TSLRuleSnippet snippet;

    protected TSLEvent event;
    protected List<TSLPredicate> predicates;
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

    protected void setAssociatedRuleset(TSLRuleset associatedRuleset) {
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
        this.predicates = snippet.getPredicateSnippets().stream()
                .map(TSLPredicateSnippet::getPredicateDefinition).collect(Collectors.toList());
        this.action = snippet.getActionSnippet().getActionDefinition();
    }

    /* ----------------------------------- */

    @Override
    public GoodieObject getAttributes() {
        return attributeList.getSquashedAttributes();
    }

    public GoodieObject getOverriddenAttributes() {
        return GoodieUtils.mergeOverriding(associatedRuleset.getAttributes(), this.getAttributes());
    }

    /* ----------------------------------- */

    public void decorate(TSLContext context, TSLDecorator decoratorDefinition, TSLDecoratorCall decoratorCall) {
        this.attributeList.loadDecorator(context, decoratorDefinition, decoratorCall);
    }

    public boolean perform(TSLContext context) {
        if (context.getEvent() != this.event) {
            return false;
        }

        context.setAttributes(getOverriddenAttributes());

        JSEngine jsEngine = context.getLanguage().getJsEngine();
        ScriptableObject scope = jsEngine.createChildScope();
        jsEngine.loadTSLContext(scope, context);
        context.setRuleScope(scope);

        for (TSLPredicateSnippet predicateSnippet : snippet.getPredicateSnippets()) {
            TSLPredicate predicate = predicateSnippet.getPredicateDefinition();
            if (!predicate.satisfies(context, predicateSnippet.getPredicateTokens())) {
                return false;
            }
        }

        List<TSLToken> actionTokens = snippet.getActionSnippet().getActionTokens();
        action.performRaw(actionTokens, context);
        return true;
    }

}
