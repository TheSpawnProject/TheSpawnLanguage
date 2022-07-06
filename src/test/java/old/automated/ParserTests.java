package old.automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.legacy.parser.TSLParserOld;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.legacy.runtime.TSLRuleOld;
import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

public class ParserTests {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
        TSL.getPluginManager().loadPlugin(new ExamplePlugin());

        TSL.getPluginManager().loadPlugin(new TSLPlugin() {
            @Override
            public void registerActions(TSLRegistry<TSLAction> registry) {
                TSLAction dummyEitherAction = new TSLAction(this, "EITHER") {
                    @Override
                    public String getUsage() {
                        return "~";
                    }

                    @Override
                    public void validateTokens(TSLToken nameToken, List<TSLToken> arguments, TSLRuleOld rule, TSLParserOld parser) throws TSLSyntaxError {}

                    @Override
                    public void perform(List<TSLToken> arguments, TSLContext context) {}
                };
                registry.register(dummyEitherAction);
            }
        });
    }

    @Test
    public void shouldParseScripts() throws IOException {
        String script = TestUtils.loadTSLScript("sample2.tsl");

        TSLParserOld parser = new TSLParserOld(TSL);
        TSLRulesetOld ruleset = parser.parse(script);

        System.out.println("/--------------------------------/");

        System.out.println("Snippets:");
        for (TSLSnippet snippet : ruleset.getSnippets()) {
            System.out.println(snippet);
        }

        System.out.println("/--------------------------------/");

        System.out.println("Tags: " + ruleset.getTags());
        System.out.println("Attrs: " + ruleset.generateAttributes());
        System.out.println("Rules: " + ruleset.getRules());
    }

}
