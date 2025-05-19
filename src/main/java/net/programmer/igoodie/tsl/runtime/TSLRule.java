package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.definition.TSLPredicate;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;

import java.util.List;
import java.util.Optional;

public class TSLRule {

    protected final TSLEvent event;
    protected final List<TSLPredicate> predicates;
    protected final TSLAction action;

    public TSLRule(TSLEvent event, List<TSLPredicate> predicates, TSLAction action) {
        this.event = event;
        this.predicates = predicates;
        this.action = action;
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

    /* ------------------- */

    public static class Ref {

        protected final TSLAction.Ref action;
        protected final String eventName;
        protected final List<TSLPredicate> predicates;

        public Ref(TSLAction.Ref action, String eventName, List<TSLPredicate> predicates) {
            this.action = action;
            this.eventName = eventName;
            this.predicates = predicates;
        }

        public Optional<TSLRule> resolve(TSLPlatform platform) {
            try {

                Optional<TSLEvent> eventOpt = platform.getEvent(this.eventName);
                if (eventOpt.isEmpty()) return Optional.empty();
                TSLEvent event = eventOpt.get();

                Optional<TSLAction> actionOpt = this.action.resolve(platform);
                if (actionOpt.isEmpty()) return Optional.empty();
                TSLAction action = actionOpt.get();

                return Optional.of(new TSLRule(event, this.predicates, action));

            } catch (Exception e) {
                return Optional.empty();
            }
        }

    }

}
