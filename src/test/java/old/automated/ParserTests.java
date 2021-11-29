package old.automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
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
        TSL.loadPlugin(new ExamplePlugin());

        TSL.loadPlugin(new TSLPlugin(new TSLPluginManifest("", "", "")) {
            @Override
            public void registerActions(TSLRegistry<TSLAction> registry) {
                TSLAction dummyEitherAction = new TSLAction(this, "EITHER") {
                    @Override
                    public String getUsage() {
                        return "~";
                    }

                    @Override
                    public void validateTokens(List<TSLToken> arguments, TSLRule rule) throws TSLSyntaxError {}

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

        TSLParser parser = new TSLParser(TSL);
        TSLRuleset ruleset = parser.parse(script);

        System.out.println("/--------------------------------/");

        System.out.println("Snippets:");
        for (TSLSnippet snippet : ruleset.getSnippets()) {
            System.out.println(snippet);
        }

        System.out.println("/--------------------------------/");

        System.out.println("Tags: " + ruleset.getTags());
        System.out.println("Attrs: " + ruleset.getAttributes());
        System.out.println("Rules: " + ruleset.getRules());
    }

}
