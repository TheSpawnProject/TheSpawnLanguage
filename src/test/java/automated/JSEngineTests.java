package automated;

import example.setup.functions.MaximumOfFunction;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.function.JSEngine;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JSEngineTests {

    @Test
    public void shouldEvaluateRunCalls() throws IOException {
        GoodieObject eventArguments = new GoodieObject();
        eventArguments.put("donation", 100.00d);

        TSLContext tslContext = new TSLContext();
        tslContext.setEventArguments(eventArguments);

        TheSpawnLanguage language = new TheSpawnLanguage();

        JSEngine jsEngine = language.getJsEngine();
        jsEngine.defineConst("PI", 3.14);
        jsEngine.loadFunction(MaximumOfFunction.INSTANCE);

        String script = "run('./src/test/resources/rhino/module.test.js')";

        String evaluation = jsEngine.evaluate(script, tslContext);
        System.out.println(evaluation);
    }

}
