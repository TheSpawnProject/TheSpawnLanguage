package unit;

public class TSLExecutorTests {

//    @Test
//    public void shouldQueueTaskTask() {
//        TSLAsyncExecutor executor = new TSLAsyncExecutor("iGoodie");
//
//        long t0 = System.currentTimeMillis();
//
//        Assertions.assertThrows(CompletionException.class,
//                () -> {
//                    TSLAsyncExecutor.Procedure<Integer> procedure = new TSLAsyncExecutor.Procedure<>(
//                            () -> {
//                                Thread.sleep(2_000);
//                                return 1;
//                            },
//                            () -> 2,
//                            () -> {
//                                throw new IllegalStateException("Wopsie");
//                            }
//                    );
//
//                    executor.executeProcedureAsync(procedure).whenComplete((result, e) -> {
//                        System.out.println("\nAccepting results: " + Thread.currentThread().getName());
//                        System.out.println("RESULT: " + result);
//                        System.out.println("EXCEPTION: " + e);
//                    }).join();
//                }
//        );
//
//        long t1 = System.currentTimeMillis();
//
//        Assertions.assertTrue(t1 - t0 >= 2000, "Took at least 2 seconds");
//    }
//
//    @Test
//    public void shouldPerformComplexRule() throws TSLSyntaxException, IOException {
//        String script = String.join(" ", "",
//                "FOR i FROM 2 TO 10 INCREASING 2",
//                " SEQUENTIALLY",
//                "  DEBUG %Executing i = ${i}%",
//                " AND",
//                "  EITHER",
//                "   WEIGHT 8 REFLECT CoconutOrange",
//                "    DEBUG %At step ${i} -> 80\\%%",
//                "  OR",
//                "   WEIGHT 2 REFLECT CoconutOrange",
//                "    DEBUG %At step ${i} -> 20\\%%",
//                "ON Donation",
//                "WITH amount = 5"
//        );
//
//        TSLPlatform platform = new TSLPlatform("Test", 1.0f);
//
//        platform.initializeStd();
//
//        platform.registerEvent(new TSLEvent("Donation")
//                .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("amount")));
//
//        platform.registerAction("DEBUG", (platform1, args1) -> new TSLAction(platform1, args1) {
//            @Override
//            public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
//                System.out.println("[DEBUG] [" + Thread.currentThread().getName() + "] [" + ctx.getTarget() + "] " + args1);
//                return true;
//            }
//        });
//
//        TSLAsyncExecutor executor = new TSLAsyncExecutor("Player:iGoodie");
//        TSLAsyncExecutor coconutExecutor = new TSLAsyncExecutor("Player:CoconutOrange");
//
//        ReflectAction.registerProvider(new ReflectAction.ReflectProvider() {
//            @Override
//            public List<String> getEventTargets(String originalTarget, List<String> targets) {
//                return targets.stream().map(x -> "Player:" + x)
//                        .collect(Collectors.toList());
//            }
//
//            @Override
//            public List<String> getAllEventTargets(String originalTarget) {
//                return null; // <- Not testing this
//            }
//
//            @Override
//            public List<String> getRandomEventTargets(String originalTarget, int count) {
//                return null; // <- Not testing this
//            }
//
//            @Override
//            public void onEventReflection(String originalTarget, TSLAction action, TSLEventContext ctx) {
//                if (!ctx.getTarget().equals("Player:CoconutOrange")) {
//                    throw new IllegalArgumentException("I am only supposed to test CoconutOrange");
//                }
//
//                coconutExecutor.executeTaskAsync(() -> action.perform(ctx));
//            }
//        });
//
//        TSLLexer lexer = new TSLLexer(CharStream.fromString(script));
//        TSLParser parser = new TSLParser(platform, "Player:iGoodie", lexer.tokenize());
//        TSLRuleset ruleset = parser.parse();
//
//        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
//        ctx.setTarget("Player:iGoodie");
//        ctx.getEventArgs().put("amount", 5.0d);
//
//        executor.executeTaskAsync(() -> ruleset.perform(ctx));
//    }

}
