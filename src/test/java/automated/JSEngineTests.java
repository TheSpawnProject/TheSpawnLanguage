package automated;

import example.setup.ExamplePlugin;
import example.setup.event.AlertEvent;
import example.setup.functions.MaximumOfFunction;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;

public class JSEngineTests {

    @Test
    public void shouldEvaluateExpr() throws IOException {
        TheSpawnLanguage language = new TheSpawnLanguage();
        language.loadPlugin(new ExamplePlugin());

        String tslScript = TestFiles.loadTSLScript("sample.expr.tsl");

        TSLParser parser = new TSLParser(language);
        TSLRuleset ruleset = parser.parse(tslScript);

        GoodieObject eventArguments = new GoodieObject();
        eventArguments.put("donation", 105);

        TSLContext tslContext = new TSLContext(language);
        tslContext.setEvent(AlertEvent.INSTANCE);
        tslContext.setEventArguments(eventArguments);

        ruleset.perform(tslContext);
    }

    @Test
    public void shouldEvaluateRunCalls() {
        TheSpawnLanguage language = new TheSpawnLanguage();

        GoodieObject eventArguments = new GoodieObject();
        eventArguments.put("donation", 100.00d);

        TSLContext tslContext = new TSLContext(language);
        tslContext.setEventArguments(eventArguments);

        JSEngine jsEngine = language.getJsEngine();
        jsEngine.defineConst("PI", 3.14);
        jsEngine.loadFunction(MaximumOfFunction.INSTANCE);

        String script = "run('./src/test/resources/rhino/module.test.js')";

        String evaluation = jsEngine.evaluate(script, tslContext);
        System.out.println(evaluation);
    }

}
