package example.setup;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.hook.TSLRulesetHook;
import net.programmer.igoodie.legacy.runtime.node.ActionNode;
import net.programmer.igoodie.legacy.runtime.node.PredicateNode;

public class ExampleHooks extends TSLRulesetHook {

    @Override
    public void onEventOccur(TSLRuleset ruleset, TSLContext context) {
        System.out.println("Event occurred!");
    }

    @Override
    public void onRuleMatched(TSLRule rule, TSLContext context) {
        System.out.println("Rule matched!");
    }

    @Override
    public void onPredicateReached(TSLRule rule, PredicateNode node, TSLContext context) {
        System.out.println("Predicate reached!");
    }

    @Override
    public void onPredicateFailed(TSLRule rule, PredicateNode node, TSLContext context) {
        System.out.println("Predicate failed!");
    }

    @Override
    public void onPredicatePassed(TSLRule rule, PredicateNode node, TSLContext context) {
        System.out.println("Predicate passed!");
    }

    @Override
    public void onActionReached(TSLRule rule, ActionNode node, TSLContext context) {
        System.out.println("Action reached!");
    }

    @Override
    public void onActionPerformed(TSLRule rule, ActionNode node, TSLContext context) {
        System.out.println("Action performed!");
    }

}
