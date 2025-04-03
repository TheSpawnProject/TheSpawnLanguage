package example;

import example.action.PrintAction;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.CharStream;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.executor.TSLAsyncExecutor;
import net.programmer.igoodie.tsl.runtime.predicate.TSLComparator;
import net.programmer.igoodie.tsl.runtime.predicate.TSLPredicate;
import net.programmer.igoodie.tsl.util.LogFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TestPlatform {

    public static final TSLPlatform platform = new TSLPlatform("TestPlatform", 1.0f);

    private static DateFormat getDateFormat(String pattern, TimeZone timezone) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(timezone);
        return dateFormat;
    }

    private static final DateFormat DATE_FORMAT = getDateFormat("dd-MM-yyyy", TimeZone.getDefault());
    private static final DateFormat TIME_FORMAT = getDateFormat("HH:mm:ss", TimeZone.getDefault());
    private static final DateFormat UTC_DATE_FORMAT = getDateFormat("dd-MM-yyyy", TimeZone.getTimeZone("UTC"));
    private static final DateFormat UTC_TIME_FORMAT = getDateFormat("HH:mm:ss", TimeZone.getTimeZone("UTC"));

    private static final TSLEvent.PropertyBuilder<Set<String>> STRING_SET_BUILDER = new TSLEvent.PropertyBuilder<>(
            (eventArgs, propertyName) -> eventArgs.getArray(propertyName)
                    .map(array -> array.stream().map(e -> e.asPrimitive().getString()).collect(Collectors.toSet())),
            (eventArgs, propertyName, value) -> eventArgs.put(propertyName, GoodieElement.fromArray(value.toArray()))
    );

    public static final TSLEvent.Property<String> ACTOR_PROPERTY = TSLEvent.PropertyBuilder.STRING.create("actor");
    public static final TSLEvent.Property<String> MESSAGE_PROPERTY = TSLEvent.PropertyBuilder.STRING.create("message");
    public static final TSLEvent.Property<Double> AMOUNT_PROPERTY = TSLEvent.PropertyBuilder.DOUBLE.create("amount");
    public static final TSLEvent.Property<Integer> MONTHS_PROPERTY = TSLEvent.PropertyBuilder.INT.create("months");
    public static final TSLEvent.Property<String> CURRENCY_PROPERTY = TSLEvent.PropertyBuilder.STRING.create("currency");
    public static final TSLEvent.Property<Set<String>> CHAT_BADGES_PROPERTY = STRING_SET_BUILDER.create("chatProperty");

    @BeforeAll()
    public static void registerEverything() {
        platform.initializeStd();

        platform.registerAction("PRINT", PrintAction::new);
        platform.registerAction("DROP", PrintAction::new);

        platform.registerExpression("event", (expr, ctx) -> Optional.of(ctx.getEventName()));
        platform.registerExpression("streamer", (expr, ctx) -> Optional.of(ctx.getTarget()));
        platform.registerExpression("actor", (expr, ctx) -> ctx.getEventArgs().getString(expr));
        platform.registerExpression("message", (expr, ctx) -> ctx.getEventArgs().getString(expr).map(LogFormatter::escapeJson));
        platform.registerExpression("message_unescaped", (expr, ctx) -> ctx.getEventArgs().getString("message"));
        platform.registerExpression("title", (expr, ctx) -> ctx.getEventArgs().getString(expr));
        platform.registerExpression("amount", (expr, ctx) -> ctx.getEventArgs().getDouble(expr).filter(num -> num != 0.0));
        platform.registerExpression("amount_i", (expr, ctx) -> ctx.getEventArgs().getDouble("amount").filter(num -> num != 0.0).map(Double::intValue));
        platform.registerExpression("amount_f", (expr, ctx) -> ctx.getEventArgs().getDouble("amount").filter(num -> num != 0.0).map(num -> String.format("%.2f", num)));
        platform.registerExpression("currency", (expr, ctx) -> ctx.getEventArgs().getString(expr));
        platform.registerExpression("months", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != 0));
        platform.registerExpression("tier", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != -1).map(num -> num == 0 ? "Prime" : String.valueOf(num)));
        platform.registerExpression("gifted", (expr, ctx) -> ctx.getEventArgs().getBoolean(expr));
        platform.registerExpression("viewers", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != 0));
        platform.registerExpression("raiders", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != 0));
        platform.registerExpression("date", (expr, ctx) -> Optional.of(DATE_FORMAT.format(new Date())));
        platform.registerExpression("date_utc", (expr, ctx) -> Optional.of(UTC_DATE_FORMAT.format(new Date())));
        platform.registerExpression("time", (expr, ctx) -> Optional.of(TIME_FORMAT.format(new Date())));
        platform.registerExpression("time_utc", (expr, ctx) -> Optional.of(UTC_TIME_FORMAT.format(new Date())));
        platform.registerExpression("unix", (expr, ctx) -> Optional.of(Instant.now().getEpochSecond()));

        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(ACTOR_PROPERTY)
                .addPropertyType(MESSAGE_PROPERTY)
                .addPropertyType(AMOUNT_PROPERTY)
                .addPropertyType(CURRENCY_PROPERTY)
        );

        platform.registerEvent(new TSLEvent("Twitch Follow")
                .addPropertyType(ACTOR_PROPERTY)
        );

        platform.registerEvent(new TSLEvent("Twitch Chat Message")
                .addPropertyType(ACTOR_PROPERTY)
                .addPropertyType(MESSAGE_PROPERTY)
                .addPropertyType(MONTHS_PROPERTY)
                .addPropertyType(CHAT_BADGES_PROPERTY)
        );
    }

    @Test
    public void shouldParse() throws IOException, TSLSyntaxException {
        String script = String.join("\n",
                "PRINT Hey %There, ${actor} ${actor}!% %How are you?% # This is a comment",
                " DISPLAYING %Thanks ${actor}, 100\\% #*100\\%*# for donating ${amount_i}${currency}!%",
                " ON Donation WITH amount IN RANGE [0,100]",
                "               ",
                "               ",
                "DROP apple",
                " ON Twitch Follow"
        );

        TSLLexer lexer = new TSLLexer(CharStream.fromString(script));
        List<TSLLexer.Token> tokens = lexer.tokenize();

        for (TSLLexer.Token token : tokens) {
            System.out.printf("%s [%s]\n", token.type, token.value
                    .replaceAll("\r", "\\\\r")
                    .replaceAll("\n", "\\\\n"));
        }

        TSLParser parser = new TSLParser(platform, "Player:iGoodie", tokens);
        TSLRuleset ruleset = parser.parse();

        Assertions.assertEquals(2, ruleset.getRules().size());
        Assertions.assertEquals("Donation", ruleset.getRules().get(0).getEvent().getName());
        Assertions.assertEquals("Twitch Follow", ruleset.getRules().get(1).getEvent().getName());
    }

    @Test
    public void shouldPerformAction() throws IOException, TSLSyntaxException, TSLPerformingException {
        // Definition of the event
        TSLEvent event = platform.getEvent("Donation")
                .orElseThrow(() -> new RuntimeException("Unknown event name"));

        // Event context, normally queued by the event generators
        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ACTOR_PROPERTY.write(ctx.getEventArgs(), "TestActor");
        AMOUNT_PROPERTY.write(ctx.getEventArgs(), 100.0);
        CURRENCY_PROPERTY.write(ctx.getEventArgs(), "USD");
        ctx.setTarget("Player:iGoodie");

        // Runtime entity representing the Action
        String actionScript = "PRINT Hey %There, ${actor} ${actor}!% %How are you?%\n" +
                " DISPLAYING %Thanks ${actor}, for donating ${amount_i}${currency}!%";
        List<TSLLexer.Token> actionPart = new TSLLexer(CharStream.fromString(actionScript)).tokenize();
        String actionName = actionPart.get(0).value;
        List<String> actionArgs = actionPart.subList(1, actionPart.size()).stream().map(e -> e.value).collect(Collectors.toList());
        TSLAction action = platform.getActionDefinition(actionName)
                .orElseThrow(() -> new RuntimeException("Unknown action name"))
                .generate(platform, actionArgs);

        // Runtime entity representing the Predicate
        String predicateScript = "amount = 100";
        List<TSLLexer.Token> predicatePart = new TSLLexer(CharStream.fromString(predicateScript)).tokenize();
        String fieldName = predicatePart.get(0).value;
        String rightValue = predicatePart.get(predicatePart.size() - 1).value;
        String symbol = predicatePart.subList(1, predicatePart.size() - 1).stream().map(e -> e.value)
                .collect(Collectors.joining(" ")).toUpperCase();
        TSLComparator comparator = platform.getComparatorDefinition(symbol)
                .orElseThrow(() -> new RuntimeException("Unknown comparator symbol"))
                .generate(rightValue);
        TSLPredicate predicate = new TSLPredicate(fieldName, comparator);

        TSLRule rule = new TSLRule(event);
        rule.setAction(action);
        rule.addPredicate(predicate);
        List<String> resultingMessage = rule.perform(ctx);
        Assertions.assertEquals("Thanks TestActor, for donating 100USD!", resultingMessage.get(0));
    }

    @Test
    public void shouldParseAndPerform() throws TSLSyntaxException, IOException {
        String script = String.join("\r\n",
                "PRINT Hey %There, ${actor} ${actor}!% %How are you?% # This is a comment",
                " DISPLAYING %Thanks ${actor}, 100\\% #*100\\%*# for donating ${amount_i}${currency}!%",
                " ON Donation WITH amount IN RANGE [0,100]",
                "               ",
                "               ",
                "PRINT apple",
                " ON Twitch Follow"
        );

        String target = "Player:iGoodie";

        List<TSLLexer.Token> tokens = new TSLLexer(CharStream.fromString(script)).tokenize();
        TSLRuleset ruleset = new TSLParser(platform, target, tokens).parse();

        TSLAsyncExecutor executor = new TSLAsyncExecutor(target);

        TSLEventContext ctx1 = new TSLEventContext(platform, "Donation");
        ACTOR_PROPERTY.write(ctx1.getEventArgs(), "TestActor");
        AMOUNT_PROPERTY.write(ctx1.getEventArgs(), 100.0);
        CURRENCY_PROPERTY.write(ctx1.getEventArgs(), "USD");
        ctx1.setTarget(target);
        CompletableFuture<List<String>> future1 = executor.executeTaskAsync(() -> ruleset.perform(ctx1))
                .whenComplete((a, b) -> System.out.println("Performed first ctx."));
        System.out.println("Queued first ctx");

        TSLEventContext ctx2 = new TSLEventContext(platform, "Twitch Follow");
        ctx2.setTarget(target);
        CompletableFuture<List<String>> future2 = executor.executeTaskAsync(() -> ruleset.perform(ctx2));
        System.out.println("Queued second ctx");

        TSLEventContext ctx3 = new TSLEventContext(platform, "Facebook Friend Request");
        ctx3.setTarget(target);
        CompletableFuture<List<String>> future3 = executor.executeTaskAsync(() -> ruleset.perform(ctx3));
        System.out.println("Queued third ctx");

        CompletableFuture.allOf(future1, future2, future3).join();

        Assertions.assertIterableEquals(
                Collections.singletonList("Thanks TestActor, 100%  for donating 100USD!"),
                future1.getNow(null));

        Assertions.assertIterableEquals(Collections.emptyList(), future2.getNow(null));

        Assertions.assertNull(future3.getNow(null));
    }

    @Test
    public void shouldPerformSequentiallyAction() throws TSLSyntaxException, IOException {
        String script = String.join("\n",
                "SEQUENTIALLY",
                "PRINT %Hello!%",
                "AND",
                "WAIT 2 seconds",
                "AND",
                "PRINT %How are you?%",
                "DISPLAYING %My message%",
                "ON Donation"
        );

        String.join("\n",
                "FOR i IN 5",
                "PRINT ${local.i}",
                "ON Donation"
        );

        String target = "Player:iGoodie";

        List<TSLLexer.Token> tokens = new TSLLexer(CharStream.fromString(script)).tokenize();
        TSLRuleset ruleset = new TSLParser(platform, target, tokens).parse();

        TSLAsyncExecutor executor = new TSLAsyncExecutor(target);

        long t0 = System.currentTimeMillis();

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ACTOR_PROPERTY.write(ctx.getEventArgs(), "TestActor");
        AMOUNT_PROPERTY.write(ctx.getEventArgs(), 100.0);
        CURRENCY_PROPERTY.write(ctx.getEventArgs(), "USD");
        ctx.setTarget(target);
        executor.executeTaskAsync(() -> ruleset.perform(ctx))
                .thenAccept((message) -> System.out.println("Message = " + message))
                .join();

        long t1 = System.currentTimeMillis();

        Assertions.assertTrue(t1 - t0 > 2000);
    }

}
