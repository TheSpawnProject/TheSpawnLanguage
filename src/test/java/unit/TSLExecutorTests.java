package unit;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.CharStream;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.executor.TSLExecutor;
import net.programmer.igoodie.tsl.std.action.ReflectAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class TSLExecutorTests {

    @Test
    public void shouldExecuteTask() {
        TSLExecutor executor = new TSLExecutor("iGoodie");

        long t0 = System.currentTimeMillis();

        Assertions.assertThrows(CompletionException.class,
                () -> {
                    TSLExecutor.Procedure<Integer> procedure = new TSLExecutor.Procedure<>(
                            () -> {
                                Thread.sleep(2_000);
                                return 1;
                            },
                            () -> 2,
                            () -> {
                                throw new IllegalStateException("Wopsie");
                            }
                    );

                    executor.resolveProcedure(procedure).whenComplete((result, e) -> {
                        System.out.println("\nAccepting results: " + Thread.currentThread().getName());
                        System.out.println("RESULT: " + result);
                        System.out.println("EXCEPTION: " + e);
                    }).join();
                }
        );

        long t1 = System.currentTimeMillis();

        Assertions.assertTrue(t1 - t0 >= 2000, "Took at least 2 seconds");
    }

    @Test
    public void shouldPerformComplexRule() throws TSLSyntaxException, IOException {
        String script = String.join(" ", "",
                "FOR i FROM 2 TO 10 INCREASING 2",
                " SEQUENTIALLY",
                "  DEBUG %Executing i = ${i}%",
                " AND",
                "  EITHER",
                "   WEIGHT 8 REFLECT CoconutOrange",
                "    DEBUG %At step ${i} -> 80\\%%",
                "  OR",
                "   WEIGHT 2 REFLECT CoconutOrange",
                "    DEBUG %At step ${i} -> 20\\%%",
                "ON Donation",
                "WITH amount = 5"
        );

        TSLPlatform platform = new TSLPlatform("Test", 1.0f);

        platform.initializeStd();

        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("amount")));

        platform.registerAction("DEBUG", (platform1, args1) -> new TSLAction(platform1, args1) {
            @Override
            public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
                System.out.println("[DEBUG] [" + Thread.currentThread().getName() + "] [" + ctx.getTarget() + "] " + args1);
                return true;
            }
        });

        TSLExecutor executor = new TSLExecutor("Player:iGoodie");
        TSLExecutor coconutExecutor = new TSLExecutor("Player:CoconutOrange");

        ReflectAction.registerProvider(new ReflectAction.ReflectProvider() {
            @Override
            public List<String> getEventTargets(String originalTarget, List<String> targets) {
                return targets.stream().map(x -> "Player:" + x)
                        .collect(Collectors.toList());
            }

            @Override
            public List<String> getAllEventTargets(String originalTarget) {
                return null; // <- Not testing this
            }

            @Override
            public List<String> getRandomEventTargets(String originalTarget, int count) {
                return null; // <- Not testing this
            }

            @Override
            public void onEventReflection(String originalTarget, TSLAction action, TSLEventContext ctx) {
                if (!ctx.getTarget().equals("Player:CoconutOrange")) {
                    throw new IllegalArgumentException("I am only supposed to test CoconutOrange");
                }

                coconutExecutor.resolveCallable(() -> action.perform(ctx));
            }
        });

        TSLLexer lexer = new TSLLexer(CharStream.fromString(script));
        TSLParser parser = new TSLParser(platform, "Player:iGoodie", lexer.tokenize());
        TSLRuleset ruleset = parser.parse();

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ctx.setTarget("Player:iGoodie");
        ctx.getEventArgs().put("amount", 5.0d);

        executor.resolveCallable(() -> ruleset.perform(ctx));
    }

}
