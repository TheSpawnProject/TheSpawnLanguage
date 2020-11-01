package example.setup.event;

import example.setup.fields.TimeField;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLEventField;

import java.util.Set;

public class AlertEvent extends TSLEvent {

    public static final AlertEvent INSTANCE = new AlertEvent();

    private AlertEvent() {
        super("Alert Event");
    }

    @Override
    public Set<TSLEventField<?>> getAcceptedFields() {
        return fieldsAsSet(
                TimeField.INSTANCE
        );
    }

}
