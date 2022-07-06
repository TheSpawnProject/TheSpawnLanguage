package automated;

import example.plugin.ExamplePlugin;
import example.plugin.event.DummyEvent;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.executor.TSLExecutor;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.Test;

public class ExecutorTests {

    @Test
    public void test() throws InterruptedException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        tsl.getPluginManager().loadPlugin(new ExamplePlugin());

        TSLParser parser = new TSLParser(tsl);

        TSLExecutor tslExecutor = new TSLExecutor("tsl-test-executor");

        String script = "#! IMPORT exampleplugin\n" +
                "WAIT ${Math.random() * 5} seconds ON Dummy Event FROM exampleplugin" +
                "\n\n" +
                "exampleplugin.PRINT ${i = 10} ${i} ON Dummy Event FROM exampleplugin" +
                "\n\n" +
                "exampleplugin.PRINT ${++i} ${$TSL_VERSION} ON Dummy Event FROM exampleplugin";

        System.out.println(script);

        TSLRuleset ruleset = parser.parse(script);

        for (int i = 0; i < 10; i++) {
            TSLContext context = new TSLContext(tsl);
            context.setEvent(DummyEvent.INSTANCE);
            context.setEventArguments(new GoodieObject());
            tslExecutor.execute(ruleset, context);
        }

        System.out.println("Executors fired @ " + Thread.currentThread());
        Thread.sleep(20 * 1000);
    }

}
