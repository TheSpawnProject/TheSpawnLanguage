package example.plugin;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.hook.TSLRulesetHook;

public class ExampleHooks extends TSLRulesetHook {

    @Override
    public void onEventOccur(TSLRuleset ruleset, TSLContext context) {
        System.out.println("Event occurred!");
    }

    @Override
    public void onRuleMatched(TSLRule rule, TSLContext context) {
        System.out.println("Rule matched!");
    }

}
