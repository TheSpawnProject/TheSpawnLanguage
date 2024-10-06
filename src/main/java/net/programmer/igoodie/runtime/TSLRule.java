package net.programmer.igoodie.runtime;

import net.programmer.igoodie.runtime.action.TSLAction;
import net.programmer.igoodie.runtime.event.TSLEvent;
import net.programmer.igoodie.runtime.event.TSLEventContext;
import net.programmer.igoodie.runtime.predicate.TSLPredicate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TSLRule {

    protected TSLEvent event;
    protected List<TSLPredicate> predicates;
    protected TSLAction action;

    public TSLRule(TSLEvent event) {
        this.event = event;
        this.predicates = new LinkedList<>();
    }

    public List<TSLPredicate> getPredicates() {
        return Collections.unmodifiableList(predicates);
    }

    public void setAction(TSLAction action) {
        if (this.action != null)
            throw new IllegalStateException("Action for this event is already set.");
        this.action = action;
    }

    public void addPredicate(TSLPredicate predicate) {
        this.predicates.add(predicate);
    }

    public List<String> perform(TSLEventContext ctx) {
        for (TSLPredicate predicate : predicates) {
            if (!predicate.test(ctx)) {
                return null;
            }
        }

        if (action.perform(ctx)) {
            return action.getCalculatedMessage(ctx);
        }

        return null;
    }

}
