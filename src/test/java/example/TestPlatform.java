package example;

import example.action.PrintAction;
import net.programmer.igoodie.TSL;
import net.programmer.igoodie.exception.TSLSyntaxException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.parser.TSLTokenizer;
import net.programmer.igoodie.runtime.TSLRule;
import net.programmer.igoodie.runtime.action.TSLAction;
import net.programmer.igoodie.runtime.event.TSLEvent;
import net.programmer.igoodie.runtime.event.TSLEventContext;
import net.programmer.igoodie.runtime.predicate.TSLPredicate;
import net.programmer.igoodie.runtime.predicate.comparator.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestPlatform {

    public static final TSL tsl = new TSL("TestPlatform");

    private static final TSLEvent.PropertyBuilder<Set<String>> STRING_SET_BUILDER = new TSLEvent.PropertyBuilder<>(
            (eventArgs, propertyName) -> eventArgs.getArray(propertyName)
                    .map(array -> array.stream().map(e -> e.asPrimitive().getString()).collect(Collectors.toSet())),
            (eventArgs, propertyName, value) -> eventArgs.put(propertyName, GoodieElement.fromArray(value.toArray()))
    );

    public static final TSLEvent.Property<String> ACTOR_PROPERTY = TSLEvent.PropertyBuilder.STRING.create("actor");
    public static final TSLEvent.Property<String> MESSAGE_PROPERTY = TSLEvent.PropertyBuilder.STRING.create("message");
    public static final TSLEvent.Property<Double> AMOUNT_PROPERTY = TSLEvent.PropertyBuilder.DOUBLE.create("amount");
    public static final TSLEvent.Property<String> CURRENCY_PROPERTY = TSLEvent.PropertyBuilder.STRING.create("currency");
    public static final TSLEvent.Property<Set<String>> CHAT_BADGES_PROPERTY = STRING_SET_BUILDER.create("chatProperty");

    @BeforeAll()
    public static void registerEverything() {
        tsl.registerAction("PRINT", PrintAction::new);

        tsl.registerComparator("IN RANGE", InRangeComparator::new);
        tsl.registerComparator("CONTAINS", ContainsComparator::new);
        tsl.registerComparator("IS", IsComparator::new);
        tsl.registerComparator("PREFIX", PrefixComparator::new);
        tsl.registerComparator("POSTFIX", PostfixComparator::new);
        tsl.registerComparator("=", EqualsComparator::new);
        tsl.registerComparator(">", GtComparator::new);
        tsl.registerComparator(">=", GteComparator::new);
        tsl.registerComparator("<", LtComparator::new);
        tsl.registerComparator("<=", LteComparator::new);

        tsl.registerEvent(new TSLEvent("Donation")
                .addPropertyType(ACTOR_PROPERTY)
                .addPropertyType(MESSAGE_PROPERTY)
                .addPropertyType(AMOUNT_PROPERTY)
                .addPropertyType(CURRENCY_PROPERTY)
        );
    }

    @Test
    public void shouldPerformAction() throws TSLSyntaxException {
        // Definition of the event
        TSLEvent event = tsl.getEvent("Donation")
                .orElseThrow(() -> new RuntimeException("Unknown event name"));

        // Event context, normally queued by the event generators
        TSLEventContext ctx = new TSLEventContext(tsl, "Donation");
        ACTOR_PROPERTY.write(ctx.getEventArgs(), "TestActor");
        AMOUNT_PROPERTY.write(ctx.getEventArgs(), 100.0);
        CURRENCY_PROPERTY.write(ctx.getEventArgs(), "USD");
        ctx.setTarget("Player:iGoodie");

        // Runtime entity representing the Action
        String actionScript = "PRINT Hey %There, ${actor} ${actor}!% %How are you?%\n" +
                " DISPLAYING %Thanks ${actor}, for donating ${amount_i}${currency}!%";
        List<String> actionPart = TSLTokenizer.tokenizeWords(actionScript);
        String actionName = actionPart.get(0);
        List<String> actionArgs = actionPart.subList(1, actionPart.size());
        TSLAction action = tsl.getActionDefinition(actionName)
                .orElseThrow(() -> new RuntimeException("Unknown action name"))
                .generate(actionArgs);

        // Runtime entity representing the Predicate
        String predicateScript = "amount = 100";
        List<String> predicatePart = TSLTokenizer.tokenizeWords(predicateScript);
        String fieldName = predicatePart.get(0);
        String rightValue = predicatePart.get(predicatePart.size() - 1);
        String symbol = String.join(" ", predicatePart.subList(1, predicatePart.size() - 1)).toUpperCase();
        TSLComparator comparator = tsl.getComparatorDefinition(symbol)
                .orElseThrow(() -> new RuntimeException("Unknown comparator symbol"))
                .generate(rightValue);
        TSLPredicate predicate = new TSLPredicate(fieldName, comparator);

        TSLRule rule = new TSLRule(event);
        rule.setAction(action);
        rule.addPredicate(predicate);
        List<String> resultingMessage = rule.perform(ctx);
        Assertions.assertEquals(resultingMessage.get(0), "Thanks TestActor, for donating 100USD!");
    }

}
