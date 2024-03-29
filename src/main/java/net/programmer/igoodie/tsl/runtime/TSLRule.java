package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.exception.TSLImplementationError;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.parser.snippet.TSLActionSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLPredicateSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLRuleSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.attribute.ContextualAttributeGenerator;
import net.programmer.igoodie.tsl.runtime.listener.TSLEmitter;
import net.programmer.igoodie.tsl.runtime.listener.TSLRuleListener;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLRule implements ContextualAttributeGenerator, TSLEmitter<TSLRuleListener> {

    protected @Nullable TSLRuleset associatedRuleset;
    protected List<TSLRuleListener> listeners;

    protected TSLRuleSnippet snippet;

    protected List<Couple<TSLDecoratorCall, TSLDecorator>> decorators;
    protected TSLAction action;
    protected TSLEvent event;
    protected List<TSLPredicate> predicates;

    public TSLRule() {
        listeners = new LinkedList<>();
        decorators = new LinkedList<>();
    }

    public void setAssociatedRuleset(@NotNull TSLRuleset ruleset) {
        this.associatedRuleset = ruleset;
    }

    public void loadSnippet(TSLRuleSnippet ruleSnippet) {
        if (this.snippet != null) {
            throw new IllegalStateException("Snippet MUST not be re-initialized");
        }

        this.snippet = ruleSnippet;
        this.action = snippet.getActionSnippet().getActionDefinition();
        this.event = snippet.getEventSnippet().getEventDefinition();
        this.predicates = snippet.getPredicateSnippets().stream()
                .map(TSLPredicateSnippet::getPredicateDefinition)
                .collect(Collectors.toList());
    }

    /* ----------------------------------- */

    public TSLRuleSnippet getSnippet() {
        return snippet;
    }

    public @Nullable TSLRuleset getAssociatedRuleset() {
        return associatedRuleset;
    }

    /* ----------------------------------- */

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context) {
        GoodieObject attributes = new GoodieObject();

        for (Couple<TSLDecoratorCall, TSLDecorator> couple : decorators) {
            TSLDecoratorCall decoratorCall = couple.getFirst();
            TSLDecorator decoratorDefinition = couple.getSecond();
            GoodieObject generatedAttributes = decoratorDefinition.generateAttributes(context, decoratorCall.getArgs());
            attributes = GoodieUtils.mergeOverriding(attributes, generatedAttributes);
        }

        return attributes;
    }

    /* ----------------------------------- */

    public List<Couple<TSLDecoratorCall, TSLDecorator>> getDecorators() {
        return Collections.unmodifiableList(decorators);
    }

    public void decorate(TSLDecoratorCall decoratorCall, TSLDecorator decoratorDefinition) {
        this.decorators.add(new Couple<>(decoratorCall, decoratorDefinition));
    }

    /* ----------------------------------- */

    @Override
    public void addListener(TSLRuleListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(TSLRuleListener listener) {
        this.listeners.remove(listener);
    }

    /* ----------------------------------- */

    public boolean perform(TSLContext context) {
        if (event == null || action == null) {
            throw new TSLImplementationError("Rule has not done construction. It has no event or action yet!");
        }

        if (context.getEvent() != this.event) {
            listeners.forEach(listener -> listener.onEventMismatch(this, context, this.event));
            return false; // Does not match the event
        }

        // Generate Attributes
        GoodieObject tagAttributes = associatedRuleset == null ? new GoodieObject() : associatedRuleset.generateAttributes();
        GoodieObject ruleAttributes = generateAttributes(context);

        // Create new JS ruleScope
        JSEngine jsEngine = context.getTsl().getJsEngine();
        JSScope ruleScope = jsEngine.getGlobalScope().fork();

        // Prepare Context
        context.setAttributes(GoodieUtils.mergeOverriding(tagAttributes, ruleAttributes));
        context.setJsScope(ruleScope);
        if (this.associatedRuleset != null && this.associatedRuleset.file != null) {
            context.setBaseDir(this.associatedRuleset.file.getParentFile());
        }

        // Run through the declared predicates
        for (TSLPredicateSnippet predicateSnippet : snippet.getPredicateSnippets()) {
            TSLPredicate predicate = predicateSnippet.getPredicateDefinition();
            if (!predicate.satisfies(context, predicateSnippet.getPredicateTokens())) {
                listeners.forEach(listener -> listener.onPredicateMismatch(this, context, predicate));
                return false;
            }
        }

        // Perform action
        List<TSLToken> actionTokens = snippet.getActionSnippet().getActionTokens();
        List<TSLToken> flattenedActionTokens = TSLActionSnippet.flatten(actionTokens, context.getCaptureSnippets());
        listeners.forEach(listener -> listener.beforeActionPerform(this, context, flattenedActionTokens));
        action.performRaw(flattenedActionTokens, context);
        listeners.forEach(listener -> listener.afterActionPerform(this, context, flattenedActionTokens));
        return true;
    }

}
