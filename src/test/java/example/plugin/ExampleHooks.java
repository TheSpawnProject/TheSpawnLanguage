package example.plugin;

import net.programmer.igoodie.legacy.hook.TSLRulesetHook;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

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
