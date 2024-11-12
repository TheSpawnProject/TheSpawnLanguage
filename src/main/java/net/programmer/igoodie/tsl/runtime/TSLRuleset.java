package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSLRuleset {

    protected final String target;

    protected List<TSLRule> rules;

    public TSLRuleset(String target) {
        this.target = target;
        this.rules = new ArrayList<>();
    }

    public List<TSLRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public void addRule(TSLRule rule) {
        this.rules.add(rule);
    }

    public List<String> perform(TSLEventContext ctx) throws TSLPerformingException {
        for (TSLRule rule : rules) {
            List<String> result = rule.perform(ctx);
            ctx.setPerformingRule(null);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
