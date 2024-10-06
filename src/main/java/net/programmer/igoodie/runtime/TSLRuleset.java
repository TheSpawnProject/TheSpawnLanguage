package net.programmer.igoodie.runtime;

import net.programmer.igoodie.runtime.event.TSLEvent;

import java.util.ArrayList;
import java.util.List;

public class TSLRuleset {

    protected final String target;

    protected List<TSLEvent> rules;

    public TSLRuleset(String target) {
        this.target = target;
        this.rules = new ArrayList<>();
    }

    public void addRule(TSLEvent event) {
        this.rules.add(event);
    }

}
