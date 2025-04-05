package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSLRuleset {

    protected final String target;

    protected List<OLD_TSLRule> rules;

    public TSLRuleset(String target) {
        this.target = target;
        this.rules = new ArrayList<>();
    }

    public List<OLD_TSLRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public void addRule(OLD_TSLRule rule) {
        this.rules.add(rule);
    }

    public List<String> perform(TSLEventContext ctx) throws TSLPerformingException {
        for (OLD_TSLRule rule : rules) {
            List<String> result = rule.perform(ctx);
            ctx.setPerformingRule(null);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
