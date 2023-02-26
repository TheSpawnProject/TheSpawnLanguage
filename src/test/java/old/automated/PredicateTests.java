//package old.automated;
//
//import example.plugin.ExamplePlugin;
//import net.programmer.igoodie.tsl.TheSpawnLanguage;
//import net.programmer.igoodie.tsl.parser.TSLParser;
//import net.programmer.igoodie.tsl.runtime.TSLRuleset;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import util.TestUtils;
//
//import java.io.IOException;
//
//public class PredicateTests {
//
//    private static TheSpawnLanguage TSL;
//
//    @BeforeAll
//    public static void init() {
//        TSL = new TheSpawnLanguage();
//        TSL.getPluginManagerOld().loadPlugin(new ExamplePlugin());
//    }
//
//    @Test
//    public void shallParseTokensCorrectly() throws IOException {
//        String script = TestUtils.loadTSLScript("predicates.tsl");
//
//        TSLParser parser = new TSLParser(TSL);
//        TSLRuleset ruleset = parser.parse(script);
//    }
//
//}
