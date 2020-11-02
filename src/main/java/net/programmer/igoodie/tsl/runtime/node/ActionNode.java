package net.programmer.igoodie.tsl.runtime.node;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;

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
        action.perform(context.getActionTokens(), context);
        return true;
    }
}
