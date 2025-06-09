package net.programmer.igoodie.tsl;

import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.runtime.action.OLD_TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.word.TSLExpression;
import net.programmer.igoodie.tsl.std.action.ConcurrentlyAction;
import net.programmer.igoodie.tsl.std.action.DoAction;
import net.programmer.igoodie.tsl.std.action.SequentiallyAction;
import net.programmer.igoodie.tsl.std.action.WaitAction;

import java.util.*;

public class TSLPlatform {

    private final String platformName;
    private final float platformVersion;

    private final Map<String, TSLAction.Supplier<?>> actionDefinitions;

    private final List<TSLExpression.Evaluator> expressionEvaluators;
    private final Map<String, TSLEvent> eventDefinitions;

    @Deprecated
    private final Map<String, OLD_TSLAction.ExpressionEvaluator> OLD_expressionEvaluators;

    public TSLPlatform(String platformName, float platformVersion) {
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.actionDefinitions = new HashMap<>();
        this.expressionEvaluators = new ArrayList<>();
        this.OLD_expressionEvaluators = new HashMap<>();
        this.eventDefinitions = new HashMap<>();
    }

    public String getPlatformName() {
        return platformName;
    }

    public float getPlatformVersion() {
        return platformVersion;
    }

    // TODO: Ways to unregister definitions; shall be very useful for the LSP

    public <T extends TSLAction.Supplier<?>> T registerAction(String name, T action) {
        this.actionDefinitions.put(StringUtilities.allUpper(name), action);
        return action;
    }

    @Deprecated
    public <T extends OLD_TSLAction.ExpressionEvaluator> T OLD_registerExpression(String expression, T evaluator) {
        this.OLD_expressionEvaluators.put(expression, evaluator);
        return evaluator;
    }

    public <T extends TSLEvent> T registerEvent(T event) {
        this.eventDefinitions.put(StringUtilities.upperFirstLetters(event.getName()), event);
        return event;
    }

    public <T extends TSLExpression.Evaluator> T registerExpressionEvaluator(T evaluator) {
        this.expressionEvaluators.add(evaluator);
        return evaluator;
    }

    /* ------------------------ */

    public Optional<TSLAction.Supplier<?>> getActionDefinition(String actionName) {
        return Optional.ofNullable(this.actionDefinitions.get(StringUtilities.allUpper(actionName)));
    }

    @Deprecated
    public Optional<OLD_TSLAction.ExpressionEvaluator> getExpressionEvaluator(String expression) {
        return Optional.ofNullable(OLD_expressionEvaluators.get(expression));
    }

    public Optional<TSLEvent> getEvent(String eventName) {
        return Optional.ofNullable(this.eventDefinitions.get(StringUtilities.upperFirstLetters(eventName)));
    }

    public List<TSLExpression.Evaluator> getExpressionEvaluators() {
        return expressionEvaluators;
    }

    @Deprecated
    public void OLD_initializeStd() {
//        this.registerAction("WAIT", WaitAction::new);
//        this.registerAction("SEQUENTIALLY", SequentiallyAction::new);
//        this.registerAction("CONCURRENTLY", ConcurrentlyAction::new);
//        this.registerAction("EITHER", EitherAction::new);
//        this.registerAction("NOTHING", NothingAction::new);
//        this.registerAction("FOR", ForAction::new);
//        this.registerAction("IF", IfAction::new);
//        this.registerAction("REFLECT", ReflectAction::new);
//
//        this.registerComparator("IN RANGE", InRangeComparator::new);
//        this.registerComparator("CONTAINS", ContainsComparator::new);
//        this.registerComparator("IS", IsComparator::new);
//        this.registerComparator("PREFIX", PrefixComparator::new);
//        this.registerComparator("POSTFIX", PostfixComparator::new);
//        this.registerComparator("=", EqualsComparator::new);
//        this.registerComparator(">", GtComparator::new);
//        this.registerComparator(">=", GteComparator::new);
//        this.registerComparator("<", LtComparator::new);
//        this.registerComparator("<=", LteComparator::new);
    }

    public void initializeStd() {
        this.registerAction("WAIT", WaitAction::new);
        this.registerAction("SEQUENTIALLY", SequentiallyAction::new);
        this.registerAction("CONCURRENTLY", ConcurrentlyAction::new);
        this.registerAction("DO", DoAction::new);
    }

}
