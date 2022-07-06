package old.automated;

import example.plugin.ExamplePlugin;
import example.plugin.event.DummyEvent;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.legacy.parser.TSLParserOld;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;

public class JSEngineTests {

    @Test
    public void shouldEvaluateExpr() throws IOException {
        TheSpawnLanguage language = new TheSpawnLanguage();
        language.getPluginManager().loadPlugin(new ExamplePlugin());

        String tslScript = TestUtils.loadTSLScript("sample.expr.tsl");

        TSLParserOld parser = new TSLParserOld(language);
        TSLRulesetOld ruleset = parser.parse(tslScript);

        GoodieObject eventArguments = new GoodieObject();
        eventArguments.put("donation", 105);

        TSLContext tslContext = new TSLContext(language);
        tslContext.setEvent(DummyEvent.INSTANCE);
        tslContext.setEventArguments(eventArguments);

        ruleset.perform(tslContext);
    }

    @Test
    public void shouldEvaluateRunCalls() {
        TheSpawnLanguage language = new TheSpawnLanguage();

        GoodieObject eventArguments = new GoodieObject();
        eventArguments.put("donation", 100.00d);

        JSEngine jsEngine = language.getJsEngine();
        jsEngine.defineConst("PI", 3.14);
//        jsEngine.loadFunction(MaximumOfFunction.INSTANCE);

        TSLContext tslContext = new TSLContext(language);
        tslContext.setJsScope(jsEngine.createChildScope());
        tslContext.setEventArguments(eventArguments);

        String script = "runScript('./src/test/resources/rhino/module.test.js')";

        String evaluation = jsEngine.evaluate(script, tslContext);
        System.out.println("Evalutation = " + evaluation);
    }

}
