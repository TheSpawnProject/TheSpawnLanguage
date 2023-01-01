package automated;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.events.ManualTriggerEvent;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.eventqueue.TSLEventBuffer;
import org.junit.jupiter.api.Test;

public class ThreadTests {

    @Test
    public void eventBufferTest() {
        TheSpawnLanguage tsl = new TheSpawnLanguage();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                tsl.getEventBuffer().offer(new TSLEventBuffer.Entry(
                        finalI + "",
                        ManualTriggerEvent.INSTANCE,
                        new GoodieObject()
                ));
            }).start();
        }

        int handled = 0;

        while (handled != 10) {
            System.out.println("Taking...");
            TSLEventBuffer.Entry take = tsl.getEventBuffer().poll();
            if (take != null) {
                System.out.println("Took: " + take.getTarget());
                handled++;
            }
        }
    }

}
