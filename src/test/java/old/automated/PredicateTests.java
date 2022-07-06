package old.automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.legacy.parser.TSLParserOld;
import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;

public class PredicateTests {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
        TSL.getPluginManager().loadPlugin(new ExamplePlugin());
    }

    @Test
    public void shallParseTokensCorrectly() throws IOException {
        String script = TestUtils.loadTSLScript("predicates.tsl");

        TSLParserOld parser = new TSLParserOld(TSL);
        TSLRulesetOld ruleset = parser.parse(script);
    }

}
