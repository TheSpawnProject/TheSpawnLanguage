package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.definition.TSLPredicate;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLRuleInterpreter extends TSLInterpreter<TSLDeferred<TSLRule>, TSLParserImpl.ReactionRuleContext> {

    protected TSLDeferred<TSLAction> action;
    protected String eventName;
    protected List<TSLPredicate> predicates = new ArrayList<>();

    @Override
    protected TSLDeferred<TSLRule> yieldValue(TSLParserImpl.ReactionRuleContext tree) {
        return platform -> {
            TSLEvent event = platform.getEvent(this.eventName)
                    .orElseThrow(() -> new TSLSyntaxException("Unknown event -> {}", this.eventName));

            TSLAction action = this.action.resolve(platform);

            for (TSLPredicate predicate : this.predicates) {
                predicate.checkEventCompatibility(event);
            }

            return new TSLRule(event, this.predicates, action);
        };
    }

    @Override
    public TSLDeferred<TSLRule> visitAction(TSLParserImpl.ActionContext ctx) {
        this.action = new TSLActionInterpreter().interpret(ctx);

        return null;
    }

    @Override
    public TSLDeferred<TSLRule> visitEventName(TSLParserImpl.EventNameContext ctx) {
        this.eventName = ctx.IDENTIFIER().stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining(" "));

        return null;
    }

    @Override
    public TSLDeferred<TSLRule> visitEventPredicate(TSLParserImpl.EventPredicateContext ctx) {
        TSLPredicate predicate = new TSLPredicateInterpreter().interpret(ctx);
        this.predicates.add(predicate);

        return null;
    }

}
