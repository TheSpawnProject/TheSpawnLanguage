package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.definition.TSLPredicate;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLDoc;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;

import java.util.List;

public class TSLRule {

    protected TSLDoc tslDoc;

    protected final TSLEvent event;
    protected final List<TSLPredicate> predicates;
    protected final TSLAction action;

    public TSLRule(TSLEvent event, List<TSLPredicate> predicates, TSLAction action) {
        this.event = event;
        this.predicates = predicates;
        this.action = action;
    }

    public TSLDoc getTslDoc() {
        return tslDoc;
    }

    public TSLAction getAction() {
        return action;
    }

    public TSLEvent getEvent() {
        return event;
    }

    public List<TSLPredicate> getPredicates() {
        return predicates;
    }

    public void attachDoc(TSLDoc tslDoc) {
        this.tslDoc = tslDoc;
    }

    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        ctx.setPerformingRule(this);

        if (!ctx.getEventName().equalsIgnoreCase(this.event.getName())) {
            return null;
        }

        for (TSLPredicate predicate : this.predicates) {
            boolean test = predicate.test(ctx);
            if (!test) return null;
        }

        return this.action.perform(ctx);
    }

}
