package net.programmer.igoodie.runtime;

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

}
