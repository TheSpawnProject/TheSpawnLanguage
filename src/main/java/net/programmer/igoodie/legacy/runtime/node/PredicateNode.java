package net.programmer.igoodie.legacy.runtime.node;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.definition.TSLEventField;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.hook.HookList;

public class PredicateNode implements RuleNode {

    protected TSLEventField<?> field;
    protected TSLComparator comparator;
    protected TSLToken righthand;

    protected RuleNode nextNode;

    public PredicateNode(TSLEventField<?> field, TSLComparator comparator, TSLToken righthand) {
        this.field = field;
        this.comparator = comparator;
        this.righthand = righthand;
    }

    public TSLEventField<?> getField() {
        return field;
    }

    public TSLComparator getComparator() {
        return comparator;
    }

    public void setNextNode(RuleNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public RuleNode getNext() {
        return nextNode;
    }

    @Override
    public boolean proceed(TSLContext context) {
        TSLRule rule = context.getRule();
        HookList hooks = rule.getAssociatedRuleset().getHookList();

        hooks.onPredicateReached(rule, this, context);

        GoodieObject eventArguments = context.getEventArguments();

        Object lefthand = field.extractValue(eventArguments);
        String righthand = this.righthand.evaluate(context);

        if (!comparator.compare(lefthand, righthand)) {
            hooks.onPredicateFailed(rule, this, context);
            return false;
        } else {
            hooks.onPredicatePassed(rule, this, context);
        }

        return nextNode.proceed(context);
    }

}
