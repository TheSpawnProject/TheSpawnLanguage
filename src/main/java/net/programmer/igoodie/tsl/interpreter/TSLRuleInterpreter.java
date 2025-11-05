package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.definition.TSLPredicate;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLRuleInterpreter extends TSLInterpreter<TSLDeferred<TSLRule>, TSLParserImpl.ReactionRuleContext> {

    protected TSLDeferred<TSLAction> action;
    protected String eventName;
    protected List<TSLPredicate> predicates = new ArrayList<>();

    @Override
    public TSLDeferred<TSLRule> yieldValue(TSLParserImpl.ReactionRuleContext tree) {
        return platform -> {
            TSLEvent event = platform.getEvent(this.eventName).orElseThrow(() ->
                    new TSLSyntaxException("Unknown event -> {}", this.eventName));

            TSLAction action = this.action.resolve(platform);
            action.interpretArguments(platform);

            return new TSLRule(event, this.predicates, action);
        };
    }

    @Override
    public TSLDeferred<TSLRule> visitAction(TSLParserImpl.ActionContext ctx) {
        this.action = new TSLActionInterpreter().interpret(ctx);

        return null;
    }

    // TODO: Visit action arguments too, and pass to

    @Override
    public TSLDeferred<TSLRule> visitEventName(TSLParserImpl.EventNameContext ctx) {
        this.eventName = ctx.IDENTIFIER().stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining(" "));

        return null;
    }

    @Override
    public TSLDeferred<TSLRule> visitPredicateExpression(TSLParserImpl.PredicateExpressionContext ctx) {
        TSLWord word = new TSLWordInterpreter().interpretWord(ctx.EXPRESSION().getSymbol());

        if (!(word instanceof TSLExpression expression)) {
            throw new TSLInternalException("Matched something other than an word in an word predicate.");
        }

        this.predicates.add(new TSLPredicate.ByExpression(expression));

        return null;
    }

    @Override
    public TSLDeferred<TSLRule> visitPredicateOperation(TSLParserImpl.PredicateOperationContext ctx) {
        String fieldName = ctx.field.getText();

        String operatorSymbol = ctx.predicateOperator().children.stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining(" "))
                .toUpperCase();

        TSLPredicate.OfBinaryOperation.Operator operator = TSLPredicate.OfBinaryOperation.Operator.bySymbol(operatorSymbol)
                .orElseThrow(() -> new TSLSyntaxException("Unknown operator -> {}", operatorSymbol));

        TSLParserImpl.PredicateWordContext predicateWordTree = ctx.predicateWord();
        TSLWord rightHandValue = new TSLWordInterpreter().visitPredicateWord(predicateWordTree);

        this.predicates.add(new TSLPredicate.OfBinaryOperation(fieldName, operator, rightHandValue));

        return null;
    }

}
