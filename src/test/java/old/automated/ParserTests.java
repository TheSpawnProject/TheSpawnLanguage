//package old.automated;
//
//import example.plugin.ExamplePlugin;
//import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
//import net.programmer.igoodie.legacy.plugin.TSLPlugin;
//import net.programmer.igoodie.tsl.TheSpawnLanguage;
//import net.programmer.igoodie.tsl.definition.TSLAction;
//import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
//import net.programmer.igoodie.tsl.parser.TSLParser;
//import net.programmer.igoodie.tsl.parser.TSLParsingContext;
//import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
//import net.programmer.igoodie.tsl.parser.token.TSLToken;
//import net.programmer.igoodie.tsl.registry.TSLRegistry;
//import net.programmer.igoodie.tsl.runtime.TSLContext;
//import net.programmer.igoodie.tsl.runtime.TSLRuleset;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import util.TestUtils;
//
//import java.io.IOException;
//
//public class ParserTests {
//
//    private static TheSpawnLanguage TSL;
//
//    @BeforeAll
//    public static void init() {
//        TSL = new TheSpawnLanguage();
//        TSL.getPluginManagerOld().loadPlugin(new ExamplePlugin());
//
//        TSL.getPluginManagerOld().loadPlugin(new TSLPlugin() {
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
//    public void shouldParseScripts() throws IOException {
//        String script = TestUtils.loadTSLScript("sample2.tsl");
//
//        TSLParser parser = new TSLParser(TSL);
//        TSLRuleset ruleset = parser.parse(script);
//
//        System.out.println("/--------------------------------/");
//
//        System.out.println("Snippets:");
//        for (TSLSnippet snippet : ruleset.getSnippets()) {
//            System.out.println(snippet);
//        }
//
//        System.out.println("/--------------------------------/");
//
//        System.out.println("Tags: " + ruleset.getTagSnippets());
//        System.out.println("Attrs: " + ruleset.generateAttributes());
//        System.out.println("Rules: " + ruleset.getRules());
//    }
//
//}
