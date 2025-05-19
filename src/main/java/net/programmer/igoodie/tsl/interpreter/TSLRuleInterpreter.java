package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLPredicate;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLRuleInterpreter extends TSLInterpreter<TSLRule.Ref, TSLParserImpl.ReactionRuleContext> {

    protected TSLAction.Ref action;
    protected String eventName;
    protected List<TSLPredicate> predicates = new ArrayList<>();

    @Override
    public TSLRule.Ref yieldValue(TSLParserImpl.ReactionRuleContext tree) {
        return new TSLRule.Ref(this.action, this.eventName, this.predicates);
    }

    @Override
    public TSLRule.Ref visitAction(TSLParserImpl.ActionContext ctx) {
        this.action = new TSLActionInterpreter().interpret(ctx);

        return null;
    }

    @Override
    public TSLRule.Ref visitEventName(TSLParserImpl.EventNameContext ctx) {
        this.eventName = ctx.IDENTIFIER().stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining(" "));

        return null;
    }

    @Override
    public TSLRule.Ref visitPredicateExpression(TSLParserImpl.PredicateExpressionContext ctx) {
        TSLWord word = new TSLWordInterpreter().parseWord(ctx.EXPRESSION().getSymbol());

        if (!(word instanceof TSLExpression expression)) {
            throw new TSLInternalException("Matched something other than an word in an word predicate.");
        }

        this.predicates.add(new TSLPredicate.ByExpression(expression));

        return null;
    }

    @Override
    public TSLRule.Ref visitPredicateOperation(TSLParserImpl.PredicateOperationContext ctx) {
        String fieldName = ctx.field.getText();

        String operatorSymbol = ctx.predicateOperator().children.stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining(" "))
                .toUpperCase();

        TSLPredicate.OfBinaryOperation.Operator operator = TSLPredicate.OfBinaryOperation.Operator.bySymbol(operatorSymbol)
                .orElseThrow(() -> new TSLSyntaxException("Unknown operator -> {}", operatorSymbol));

        TerminalNode predicateWordNode = (TerminalNode) ctx.predicateWord().getChild(0);
        TSLWord rightHandValue = new TSLWordInterpreter().parseWord(predicateWordNode.getSymbol());

        this.predicates.add(new TSLPredicate.OfBinaryOperation(fieldName, operator, rightHandValue));

        return null;
    }

}
