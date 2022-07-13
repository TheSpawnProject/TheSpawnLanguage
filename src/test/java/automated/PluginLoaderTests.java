package automated;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class PluginLoaderTests {

    @Test
    public void test() throws IOException, URISyntaxException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();

        // Loads the plugin from JAR
        URL pluginURL = TestUtils.pluginURL("example-0.0.1.jar");
        tsl.getPluginManager().loadPlugin(pluginURL.toURI());

        // Parse the ruleset
        TSLParser parser = new TSLParser(tsl);
        String script = TestUtils.loadTSLScript("ruleset.test.tsl");

        System.out.println(script);

        TSLRuleset ruleset = parser.parse(script);

        // Create a context with the event in it
        TSLContext context = new TSLContext(tsl);

        TSLEvent event = tsl.getDefinition(tsl.EVENT_REGISTRY, "example-plugin:Dummy Event");
        Assertions.assertNotNull(event);
        context.setEvent(event);

        GoodieObject eventArgs = new GoodieObject();
        eventArgs.put("time", 5);
        context.setEventArguments(eventArgs);

        // And then run the context in parsed ruleset
        ruleset.perform(context);
    }

}
