package example;

import example.action.PrintAction;
import net.programmer.igoodie.TSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestPlatform {

    public static final TSL tsl = new TSL("TestPlatform");

    @BeforeAll()
    public static void registerEverything() {
        tsl.registerAction("PRINT", PrintAction::new);
    }

    @Test
    public void foo() {
        // TODO: test parsed thingies
    }

}
