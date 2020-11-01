package example.setup;

import example.setup.event.NotifiedEvent;
import example.setup.fields.TimeField;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import org.junit.Test;

public class ExampleTest {

    @Test
    public void test() {
        TheSpawnLanguage tsl = new TheSpawnLanguage();

        tsl.EVENT_FIELD_REGISTRY.register(TimeField.INSTANCE);

        tsl.EVENT_REGISTRY.register(NotifiedEvent.INSTANCE);

        System.out.println(tsl.EVENT_REGISTRY.get("Notified Event").getAcceptedFields());
    }

}
