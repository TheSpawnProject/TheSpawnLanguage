package net.programmer.igoodie.tsl;

import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.runtime.predicate.TSLComparator;
import net.programmer.igoodie.tsl.std.action.NothingAction;
import net.programmer.igoodie.tsl.std.action.SequentiallyAction;
import net.programmer.igoodie.tsl.std.action.WaitAction;
import net.programmer.igoodie.tsl.std.comparator.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TSLPlatform {

    private final String platformName;
    private final float platformVersion;

    private final Map<String, TSLAction.Supplier<?>> actionDefinitions;
    private final Map<String, TSLAction.ExpressionEvaluator> expressionEvaluators;
    private final Map<String, TSLEvent> eventDefinitions;
    private final Map<String, TSLComparator.Supplier<?>> comparatorDefinitions;

    public TSLPlatform(String platformName, float platformVersion) {
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.actionDefinitions = new HashMap<>();
        this.expressionEvaluators = new HashMap<>();
        this.eventDefinitions = new HashMap<>();
        this.comparatorDefinitions = new HashMap<>();
    }

    public String getPlatformName() {
        return platformName;
    }

    public float getPlatformVersion() {
        return platformVersion;
    }

    public <T extends TSLAction.Supplier<?>> T registerAction(String name, T action) {
        this.actionDefinitions.put(name.toUpperCase(), action);
        return action;
    }

    public <T extends TSLAction.ExpressionEvaluator> T registerExpression(String expression, T evaluator) {
        this.expressionEvaluators.put(expression, evaluator);
        return evaluator;
    }

    public <T extends TSLEvent> T registerEvent(T event) {
        this.eventDefinitions.put(event.getName(), event);
        return event;
    }

    public <T extends TSLComparator.Supplier<?>> T registerComparator(String symbol, T comparator) {
        this.comparatorDefinitions.put(symbol.toUpperCase(), comparator);
        return comparator;
    }

    public Optional<TSLAction.Supplier<?>> getActionDefinition(String name) {
        return Optional.ofNullable(this.actionDefinitions.get(name.toUpperCase()));
    }

    public Optional<TSLAction.ExpressionEvaluator> getExpressionEvaluator(String expression) {
        return Optional.ofNullable(expressionEvaluators.get(expression));
    }

    public Optional<TSLEvent> getEvent(String eventName) {
        return Optional.ofNullable(this.eventDefinitions.get(StringUtilities.upperFirstLetters(eventName)));
    }

    public Optional<TSLComparator.Supplier<?>> getComparatorDefinition(String symbol) {
        return Optional.ofNullable(this.comparatorDefinitions.get(symbol.toUpperCase()));
    }

    public void initializeStd() {
        this.registerAction("WAIT", WaitAction::new);
        this.registerAction("SEQUENTIALLY", SequentiallyAction::new);
        // TODO: EITHER
        this.registerAction("NOTHING", NothingAction::new);
        // TODO: FOR <N> TIMES
        // TODO: FOR i IN RANGE 2 30 3 DO
        // TODO: IF <C> THEN <A> [ELSE <A>]
        // TODO: REFLECT

        this.registerComparator("IN RANGE", InRangeComparator::new);
        this.registerComparator("CONTAINS", ContainsComparator::new);
        this.registerComparator("IS", IsComparator::new);
        this.registerComparator("PREFIX", PrefixComparator::new);
        this.registerComparator("POSTFIX", PostfixComparator::new);
        this.registerComparator("=", EqualsComparator::new);
        this.registerComparator(">", GtComparator::new);
        this.registerComparator(">=", GteComparator::new);
        this.registerComparator("<", LtComparator::new);
        this.registerComparator("<=", LteComparator::new);
    }

}
