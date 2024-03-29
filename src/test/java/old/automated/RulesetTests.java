//package old.automated;
//
//import example.plugin.ExamplePlugin;
//import example.plugin.event.DummyEvent;
//import net.programmer.igoodie.goodies.runtime.GoodieObject;
//import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
//import net.programmer.igoodie.legacy.plugin.TSLPlugin;
//import net.programmer.igoodie.legacy.plugin.TSLPluginManifest;
//import net.programmer.igoodie.tsl.TheSpawnLanguage;
//import net.programmer.igoodie.tsl.definition.TSLAction;
//import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
//import net.programmer.igoodie.tsl.parser.TSLParser;
//import net.programmer.igoodie.tsl.parser.TSLParsingContext;
//import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
//import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
//import net.programmer.igoodie.tsl.parser.token.TSLToken;
//import net.programmer.igoodie.tsl.registry.TSLRegistry;
//import net.programmer.igoodie.tsl.runtime.TSLContext;
//import net.programmer.igoodie.tsl.runtime.TSLRuleset;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import util.TestUtils;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class RulesetTests {
//
//    private static TheSpawnLanguage TSL;
//
//    @BeforeAll
//    public static void init() {
//        TSL = new TheSpawnLanguage();
//        TSL.getPluginManagerOld().loadPlugin(new ExamplePlugin());
//
//
//        TSL.getPluginManagerOld().loadPlugin(new TSLPlugin(new TSLPluginManifest("sample", "Sample", "0.0.0", "iGoodie")) {
//
//            @Override
//            public void registerActions(TSLRegistry<TSLAction> registry) {
//                TSLAction dummyEitherAction = new TSLAction(this, "EITHER") {
//                    @Override
//                    public String getUsage() {
//                        return "~";
//                    }
//
//                    @Override
//                    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {}
//
//                    @Override
//                    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {}
//                };
//                registry.register(dummyEitherAction);
//            }
//        });
//    }
//
//    @Test
//    public void foo() throws IOException {
//        String script = TestUtils.loadTSLScript("sample2.tsl");
//
//        TSLLexer lexer = new TSLLexer(script).lex();
//
//        for (TSLTokenBuffer snippet : lexer.getSnippets()) {
//            System.out.println(snippet);
//        }
//
//        System.out.println("\n--------------------\n");
//
//        TSLParser parser = new TSLParser(TSL);
//        TSLRuleset ruleset = parser.parse(script);
//
//        System.out.println("Rule count: " + ruleset.getRules().size());
//
//        TSLContext context = new TSLContext(TSL);
//        context.setEvent(DummyEvent.INSTANCE);
//        context.setEventArguments(new GoodieObject());
//
//        for (List<TSLToken> tslTokens : ruleset.getRules().stream()
//                .map(rule -> rule.getSnippet().getAllTokens())
//                .collect(Collectors.toList())) {
//            System.out.println(tslTokens);
//        }
//
////        boolean performed = ruleset.perform(context);
////        System.out.println("Performed: " + performed);
//    }
//
//}
