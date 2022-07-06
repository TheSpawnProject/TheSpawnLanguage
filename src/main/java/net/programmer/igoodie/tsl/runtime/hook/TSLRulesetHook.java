package net.programmer.igoodie.tsl.runtime.hook;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.legacy.runtime.TSLRuleOld;
import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;

// TODO: Rethink and reimplement
public abstract class TSLRulesetHook {

    public void onEventOccur(TSLRulesetOld ruleset, TSLContext context) {}

    public void onRuleMatched(TSLRuleOld rule, TSLContext context) {}

//    public void onPredicateReached(TSLRule rule, PredicateNode node, TSLContext context) {}
//
//    public void onPredicateFailed(TSLRule rule, PredicateNode node, TSLContext context) {}
//
//    public void onPredicatePassed(TSLRule rule, PredicateNode node, TSLContext context) {}
//
//    public void onActionReached(TSLRule rule, ActionNode node, TSLContext context) {}
//
//    public void onActionPerformed(TSLRule rule, ActionNode node, TSLContext context) {}

}
