package automated;

import example.plugin.ExamplePlugin;
import example.plugin.event.DummyEvent;
import net.programmer.igoodie.goodies.format.GsonGoodieFormat;
import net.programmer.igoodie.plugins.grammar.events.ManualTriggerEvent;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.net.URISyntaxException;

public class RulesetTests {

    @Test
    public void importsShouldNotCrash() throws URISyntaxException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        tsl.getPluginManager().loadPlugin(new ExamplePlugin());

        System.out.println(tsl.getPluginManager().LOADED_PLUGIN_IDS);

        TSLParser parser = new TSLParser(tsl);
        TSLRuleset ruleset = parser.parse(TestUtils.scriptPath("test.import.tsl"));

        TSLContext context;

        context = new TSLContext(tsl);
        context.setEvent(ManualTriggerEvent.INSTANCE);
        ruleset.perform(context);

        context = new TSLContext(tsl);
        context.setEvent(DummyEvent.INSTANCE);
        ruleset.perform(context);
    }

    @Test
    public void commentsInbetweenShouldNotCrash() throws URISyntaxException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        tsl.getPluginManager().loadPlugin(new ExamplePlugin());

        TSLParser parser = new TSLParser(tsl);
        TSLRuleset ruleset = parser.parse(TestUtils.scriptPath("comment-inbetween.tsl"));

        TSLContext context;

        context = new TSLContext(tsl);
        context.setEvent(ManualTriggerEvent.INSTANCE);
        context.setEventArguments(new GsonGoodieFormat().readGoodieFromString("{'debug_data': {'value': 1}}"));
        ruleset.perform(context);
    }

}
