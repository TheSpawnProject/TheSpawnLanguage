package example.plugin.event;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DummyEvent extends TSLEvent {

    public static final DummyEvent INSTANCE = new DummyEvent();

    private DummyEvent() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "Dummy Event");
    }

    @Override
    public @NotNull Map<String, Class<?>> getAcceptedFields() {
        return eventFields(
                new Couple<>("time", Long.class)
        );
    }

}
