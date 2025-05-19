package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLCaptureId;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.*;

public class TSLRuleset {

    protected final String target;

    protected Map<String, TSLCapture> captures;

    protected List<TSLRule> rules;

    public TSLRuleset(String target) {
        this.target = target;
        this.captures = new HashMap<>();
        this.rules = new ArrayList<>();
    }

    public String getTarget() {
        return target;
    }

    public List<TSLRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public TSLRule addRule(TSLRule rule) {
        this.rules.add(rule);
        return rule;
    }

    public TSLCapture addCapture(TSLCapture capture) {
        this.captures.put(capture.id.getCaptureName(), capture);
        return capture;
    }

    public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
        ctx.setPerformingRuleset(this);

        for (TSLRule rule : rules) {
            List<TSLWord> yield = rule.perform(ctx);
            ctx.setPerformingRule(null);

            if (yield != null) {
                Either<TSLCaptureId, TSLExpression> yieldConsumer = rule.getAction().getYieldConsumer();
                // TODO: do something with yieldConsumer
                return yield;
            }
        }

        return null;
    }

}
