package net.programmer.igoodie.runtime.event;

import net.programmer.igoodie.runtime.action.TSLAction;
import net.programmer.igoodie.runtime.predicate.TSLPredicate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TSLEvent {

    protected String name;
    protected List<TSLPredicate> predicates;
    protected TSLAction action;

    public TSLEvent(String name) {
        this.name = name;
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

    public boolean perform(TSLEventContext ctx) {
        for (TSLPredicate predicate : predicates) {
            if (!predicate.test(ctx)) {
                return false;
            }
        }
        return action.perform(ctx);
    }

}
