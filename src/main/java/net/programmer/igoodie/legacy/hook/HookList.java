package net.programmer.igoodie.legacy.hook;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.LinkedList;
import java.util.List;

@Deprecated
public final class HookList extends TSLRulesetHook {

    private final List<TSLRulesetHook> hookList;

    public HookList() {
        this.hookList = new LinkedList<>();
    }

    public void bind(TSLRulesetHook hook) {
        this.hookList.add(hook);
    }

    /* --------------------------------------- */

    @Override
    public void onEventOccur(TSLRuleset ruleset, TSLContext context) {
        for (TSLRulesetHook hook : hookList) {
            hook.onEventOccur(ruleset, context);
        }
    }

    @Override
    public void onRuleMatched(TSLRule rule, TSLContext context) {
        for (TSLRulesetHook hook : hookList) {
            hook.onRuleMatched(rule, context);
        }
    }

//    @Override
//    public void onPredicateReached(TSLRule rule, PredicateNode node, TSLContext context) {
//        for (TSLRulesetHook hook : hookList) {
//            hook.onPredicateReached(rule, node, context);
//        }
//    }
//
//    @Override
//    public void onPredicateFailed(TSLRule rule, PredicateNode node, TSLContext context) {
//        for (TSLRulesetHook hook : hookList) {
//            hook.onPredicateFailed(rule, node, context);
//        }
//    }
//
//    @Override
//    public void onPredicatePassed(TSLRule rule, PredicateNode node, TSLContext context) {
//        for (TSLRulesetHook hook : hookList) {
//            hook.onPredicatePassed(rule, node, context);
//        }
//    }
//
//    @Override
//    public void onActionReached(TSLRule rule, ActionNode node, TSLContext context) {
//        for (TSLRulesetHook hook : hookList) {
//            hook.onActionReached(rule, node, context);
//        }
//    }
//
//    @Override
//    public void onActionPerformed(TSLRule rule, ActionNode node, TSLContext context) {
//        for (TSLRulesetHook hook : hookList) {
//            hook.onActionPerformed(rule, node, context);
//        }
//    }

}
