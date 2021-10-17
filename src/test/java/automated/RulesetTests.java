package automated;

import example.plugin.ExamplePlugin;
import example.plugin.event.DummyEvent;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class RulesetTests {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
        TSL.loadPlugin(new ExamplePlugin());
    }

    @Test
    public void ruleset1() throws IOException {
        String script = TestFiles.loadTSLScript("ruleset.test.tsl");

        TSLParser parser = new TSLParser(TSL);
        TSLRuleset ruleset = parser.parse(script);

        GoodieObject eventArguments = new GoodieObject();
        eventArguments.put("time", 5);

        TSLContext context = new TSLContext(TSL);
        context.setEvent(DummyEvent.INSTANCE);
        context.setEventArguments(eventArguments);

        ruleset.perform(context);
    }

}
