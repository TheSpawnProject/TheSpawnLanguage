package net.programmer.igoodie.plugins.events.common.events;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.plugins.events.common.CommonEvents;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DonationEvent extends TSLEvent {

    public static final DonationEvent INSTANCE = new DonationEvent();

    private DonationEvent() {
        super(CommonEvents.PLUGIN_INSTANCE, "Donation");
    }

    @Override
    public @NotNull Map<String, Class<?>> getAcceptedFields() {
        return eventFields(
                new Couple<>("actor", String.class),
                new Couple<>("message", String.class),
                new Couple<>("amount", Double.class),
                new Couple<>("currency", String.class)
        );
    }

}
