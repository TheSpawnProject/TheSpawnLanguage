package net.programmer.igoodie.tsl.runtime.node_old;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.hook.HookList;

public class EventNode implements RuleNode {

    protected TSLEvent event;

    protected RuleNode nextNode;

    public EventNode(TSLEvent event) {
        this.event = event;
    }

    public TSLEvent getEvent() {
        return event;
    }

    @Override
    public RuleNode getNext() {
        return null;
    }

    public void setNextNode(RuleNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public boolean proceed(TSLContext context) {
        TSLRule rule = context.getRule();
        HookList hooks = rule.getAssociatedRuleset().getHookList();

        hooks.onRuleMatched(rule, context);

        return nextNode.proceed(context);
    }

}
