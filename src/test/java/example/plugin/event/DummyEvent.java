package example.plugin.event;

import example.plugin.ExamplePlugin;
import example.plugin.fields.TimeField;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLEventField;

import java.util.Set;

public class DummyEvent extends TSLEvent {

    public static final DummyEvent INSTANCE = new DummyEvent();

    private DummyEvent() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "Dummy Event");
    }

    @Override
    public Set<TSLEventField<?>> getAcceptedFields() {
        return eventFields(
                TimeField.INSTANCE
        );
    }

}
