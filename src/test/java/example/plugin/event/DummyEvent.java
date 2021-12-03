package example.plugin.event;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLEvent;

import java.util.Set;

public class DummyEvent extends TSLEvent {

    public static final DummyEvent INSTANCE = new DummyEvent();

    private DummyEvent() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "Dummy Event");
    }

    @Override
    public Set<String> getAcceptedFields() {
        return eventFields(
                "time"
        );
    }

}
