package net.programmer.igoodie.tsl.runtime.hook;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

// TODO: Rethink and reimplement
public abstract class TSLRulesetHook {

    public void onEventOccur(TSLRuleset ruleset, TSLContext context) {}

    public void onRuleMatched(TSLRule rule, TSLContext context) {}

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
