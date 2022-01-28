package coconut.plugin.event;

import coconut.plugin.CoconutPlugin;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CoconutFeastEvent extends TSLEvent {

    public static final CoconutFeastEvent INSTANCE = new CoconutFeastEvent();

    private CoconutFeastEvent() {
        super(CoconutPlugin.INSTANCE, "Coconut Feast Event");
    }

    @Override
    public @NotNull Map<String, Class<?>> getAcceptedFields() {
        return eventFields(
                new Couple<>("coconutsEaten", Number.class)
        );
    }

}
