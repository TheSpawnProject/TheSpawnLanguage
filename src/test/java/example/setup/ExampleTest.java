package example.setup;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import org.junit.Test;

public class ExampleTest {

    @Test
    public void test() {
        TheSpawnLanguage tsl = new TheSpawnLanguage();

        tsl.loadPlugin(new ExamplePlugin());

        System.out.println(tsl.getJsEngine().evaluate("currentUnix() + ' ' + maximumOf(5,10)"));
    }

}
