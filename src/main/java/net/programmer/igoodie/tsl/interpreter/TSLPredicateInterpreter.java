package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.definition.TSLPredicate;
import net.programmer.igoodie.tsl.runtime.token.TSLExpression;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.stream.Collectors;

public class TSLPredicateInterpreter extends TSLInterpreter<TSLPredicate, TSLParserImpl.EventPredicateContext> {

    protected TSLPredicate predicate;

    @Override
    protected TSLPredicate yieldValue(TSLParserImpl.EventPredicateContext tree) {
        return this.predicate;
    }

    @Override
    public TSLPredicate visitPredicateExpression(TSLParserImpl.PredicateExpressionContext ctx) {
        TSLToken token = new TSLTokenInterpreter().interpretToken(ctx.EXPRESSION().getSymbol());

        if (!(token instanceof TSLExpression expression)) {
            throw new TSLInternalException("Expected an expression.");
        }

        this.predicate = new TSLPredicate.ExpressionPredicate(expression);

        return null;
    }

    @Override
    public TSLPredicate visitPredicateOperation(TSLParserImpl.PredicateOperationContext ctx) {
        String fieldName = ctx.field.getText();

        String operatorSymbol = ctx.predicateOperator().children.stream()
                .map(ParseTree::getText)
                .collect(Collectors.joining(" "))
                .toUpperCase();

        TSLPredicate.BinaryOperationPredicate.Operator operator = TSLPredicate.BinaryOperationPredicate.Operator.bySymbol(operatorSymbol)
                .orElseThrow(() -> new TSLSyntaxException("Unknown operator -> {}", operatorSymbol));

        TSLParserImpl.PredicateWordContext predicateWordTree = ctx.predicateWord();
        TSLToken rightHandValue = new TSLTokenInterpreter().visitPredicateWord(predicateWordTree);

        this.predicate = new TSLPredicate.BinaryOperationPredicate(fieldName, operator, rightHandValue);

        return null;
    }

}
