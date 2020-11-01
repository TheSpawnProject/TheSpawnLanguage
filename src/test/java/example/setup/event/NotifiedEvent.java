package example.setup.event;

import example.setup.fields.TimeField;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLEventField;

import java.util.Set;

public class NotifiedEvent extends TSLEvent {

    public static final NotifiedEvent INSTANCE = new NotifiedEvent();

    private NotifiedEvent() {
        super("Notified Event");
    }

    @Override
    public Set<TSLEventField<?>> getAcceptedFields() {
        return fieldsAsSet(
                TimeField.INSTANCE
        );
    }

}
