package example.setup.event;

import example.setup.ExamplePlugin;
import example.setup.fields.TimeField;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLEventField;

import java.util.Set;

public class AlertEvent extends TSLEvent {

    public static final AlertEvent INSTANCE = new AlertEvent();

    private AlertEvent() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "Alert Event");
    }

    @Override
    public Set<TSLEventField<?>> getAcceptedFields() {
        return fieldsAsSet(
                TimeField.INSTANCE
        );
    }

}
