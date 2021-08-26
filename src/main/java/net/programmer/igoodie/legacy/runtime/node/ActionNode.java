package net.programmer.igoodie.legacy.runtime.node;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.hook.HookList;

public class ActionNode implements RuleNode {

    protected TSLAction action;

    public ActionNode(TSLAction action) {
        this.action = action;
    }

    @Override
    public RuleNode getNext() {
        return null;
    }

    @Override
    public boolean proceed(TSLContext context) {
        TSLRule rule = context.getRule();
        HookList hooks = rule.getAssociatedRuleset().getHookList();

        hooks.onActionReached(rule, this, context);

//        action.perform(context.getActionTokens(), context);

        hooks.onActionPerformed(rule, this, context);

        return true;
    }
}
