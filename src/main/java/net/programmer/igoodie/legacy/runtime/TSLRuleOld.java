package net.programmer.igoodie.legacy.runtime;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.parser.snippet.TSLPredicateSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLRuleSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.attribute.ContextualAttributeGenerator;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.ScriptableObject;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class TSLRuleOld implements ContextualAttributeGenerator {

    @Nullable
    protected TSLRulesetOld associatedRuleset;

    protected TSLRuleSnippet snippet;

    protected List<Couple<TSLDecoratorCall, TSLDecorator>> decorators;

    protected TSLEvent event;
    protected List<TSLPredicate> predicates;
    protected TSLAction action;

    public TSLRuleOld() {
        this.decorators = new LinkedList<>();
    }

    public TSLRuleOld(@NotNull TSLRulesetOld ruleset) {
        this();
        this.associatedRuleset = ruleset;
    }

    public TSLRuleOld(TSLRulesetOld ruleset, TSLRuleSnippet snippet) {
        this(ruleset);
        this.setSnippet(snippet);
    }

    public @Nullable TSLRulesetOld getAssociatedRuleset() {
        return associatedRuleset;
    }

    protected void setAssociatedRuleset(@NotNull TSLRulesetOld associatedRuleset) {
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

    public void decorate(TSLDecorator decoratorDefinition, TSLDecoratorCall decoratorCall) {
        this.decorators.add(new Couple<>(decoratorCall, decoratorDefinition));
    }

    public boolean perform(TSLContext context) {
        if (context.getEvent() != this.event) {
            return false;
        }

        // Generate and bind attributes
        GoodieObject tagAttributes = associatedRuleset == null ? new GoodieObject() : associatedRuleset.generateAttributes();
        GoodieObject ruleAttributes = generateAttributes(context);
        context.setAttributes(GoodieUtils.mergeOverriding(tagAttributes, ruleAttributes));

        // Bind JS engine scope and context
        JSEngine jsEngine = context.getTsl().getJsEngine();
        ScriptableObject scope = jsEngine.createChildScope();
        context.setJsScope(scope);
        jsEngine.loadTSLContext(scope, context);

        // Run through the declared predicates
        for (TSLPredicateSnippet predicateSnippet : snippet.getPredicateSnippets()) {
            TSLPredicate predicate = predicateSnippet.getPredicateDefinition();
            if (!predicate.satisfies(context, predicateSnippet.getPredicateTokens())) {
                return false;
            }
        }

        // Perform action
        List<TSLToken> actionTokens = snippet.getActionSnippet().getActionTokens();
        action.performRaw(actionTokens, context);
        return true;
    }

}
