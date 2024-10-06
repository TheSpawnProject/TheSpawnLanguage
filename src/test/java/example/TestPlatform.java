package example;

import example.action.PrintAction;
import net.programmer.igoodie.TSL;
import net.programmer.igoodie.exception.TSLSyntaxException;
import net.programmer.igoodie.parser.TSLTokenizer;
import net.programmer.igoodie.runtime.TSLRule;
import net.programmer.igoodie.runtime.event.TSLEvent;
import net.programmer.igoodie.runtime.event.TSLEventContext;
import net.programmer.igoodie.runtime.predicate.TSLPredicate;
import net.programmer.igoodie.runtime.predicate.comparator.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TestPlatform {

    public static final TSL tsl = new TSL("TestPlatform");


    public static final TSLEvent.PropertyType.Property<String> ACTOR = TSLEvent.PropertyType.STRING.create("actor");

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
                .addPropertyType(TSLEvent.PropertyType.STRING.create("actor"))
                .addPropertyType(TSLEvent.PropertyType.STRING.create("message"))
                .addPropertyType(TSLEvent.PropertyType.DOUBLE.create("amount"))
                .addPropertyType(TSLEvent.PropertyType.STRING.create("currency"))
        );

        tsl.registerEventFieldExtractor("actor", eventArgs -> eventArgs.getString("actor"));
        tsl.registerEventFieldExtractor("message", eventArgs -> eventArgs.getString("message"));
        tsl.registerEventFieldExtractor("currency", eventArgs -> eventArgs.getString("currency"));
        tsl.registerEventFieldExtractor("donation_currency", eventArgs -> eventArgs.getString("currency"));
        tsl.registerEventFieldExtractor("amount", eventArgs -> eventArgs.getDouble("amount"));
        tsl.registerEventFieldExtractor("donation_amount", eventArgs -> eventArgs.getDouble("amount"));
        tsl.registerEventFieldExtractor("months", eventArgs -> eventArgs.getInteger("months"));
        tsl.registerEventFieldExtractor("subscription_months", eventArgs -> eventArgs.getInteger("months"));
        tsl.registerEventFieldExtractor("tier", eventArgs -> eventArgs.getInteger("tier"));
        tsl.registerEventFieldExtractor("subscription_tier", eventArgs -> eventArgs.getInteger("tier"));
        tsl.registerEventFieldExtractor("gifted", eventArgs -> eventArgs.getBoolean("gifted"));
        tsl.registerEventFieldExtractor("viewers", eventArgs -> eventArgs.getInteger("viewerCount"));
        tsl.registerEventFieldExtractor("viewer_count", eventArgs -> eventArgs.getInteger("viewerCount"));
        tsl.registerEventFieldExtractor("raiders", eventArgs -> eventArgs.getInteger("raiderCount"));
        tsl.registerEventFieldExtractor("raider_count", eventArgs -> eventArgs.getInteger("raiderCount"));
        tsl.registerEventFieldExtractor("title", eventArgs -> eventArgs.getString("title"));
        tsl.registerEventFieldExtractor("rewardTitle", eventArgs -> eventArgs.getString("title"));
        tsl.registerEventFieldExtractor("badges", eventArgs -> eventArgs.getArray("chatBadges")
                .map(array -> array.stream().map(e -> e.asPrimitive().get()).collect(Collectors.toSet())));
        tsl.registerEventFieldExtractor("chat_badges", eventArgs -> eventArgs.getArray("chatBadges")
                .map(array -> array.stream().map(e -> e.asPrimitive().get()).collect(Collectors.toSet())));
    }

    @Test
    public void shouldPerformAction() throws TSLSyntaxException {
        // Definition of the event
        TSLEvent event = tsl.getEvent("Donation");

        // Event context, normally run by the event generators
        TSLEventContext ctx = new TSLEventContext(tsl, "Donation");
        ctx.setTarget("Player:iGoodie");
        ctx.getEventArgs().put("actor", "TestActor");
        ctx.getEventArgs().put("amount", 100);
        ctx.getEventArgs().put("currency", "USD");

        // Runtime entity representing the Action
        String actionScript = "PRINT Hey %There, ${actor} ${actor}!% %How are you?%\n" +
                " DISPLAYING %Thanks ${actor}, for donating ${amount_i}${currency}!%";
        List<String> actionPart = TSLTokenizer.tokenizeWords(actionScript);
        List<String> actionArgs = actionPart.subList(1, actionPart.size());
        PrintAction action = new PrintAction(actionArgs);

        // Runtime entity representing the Predicate
        String predicateScript = "amount = 100";
        List<String> predicatePart = TSLTokenizer.tokenizeWords(predicateScript);
        String fieldName = predicatePart.get(0);
        String rightValue = predicatePart.get(predicatePart.size() - 1);
        String symbol = String.join(" ", predicatePart.subList(1, predicatePart.size() - 1)).toUpperCase();
        TSLComparator comparator = tsl.getComparatorDefinition(symbol).generate(rightValue);
        TSLPredicate predicate = new TSLPredicate(fieldName, comparator);

        TSLRule rule = new TSLRule(event);
        rule.setAction(action);
        rule.addPredicate(predicate);
        List<String> resultingMessage = rule.perform(ctx);
        Assertions.assertEquals(resultingMessage.get(0), "Thanks TestActor, for donating 100USD!");
    }

}
