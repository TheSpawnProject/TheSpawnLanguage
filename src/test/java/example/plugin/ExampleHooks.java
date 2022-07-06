package example.plugin;

import net.programmer.igoodie.legacy.runtime.TSLRuleOld;
import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.hook.TSLRulesetHook;

public class ExampleHooks extends TSLRulesetHook {

    @Override
    public void onEventOccur(TSLRulesetOld ruleset, TSLContext context) {
        System.out.println("Event occurred!");
    }

    @Override
    public void onRuleMatched(TSLRuleOld rule, TSLContext context) {
        System.out.println("Rule matched!");
    }

}
