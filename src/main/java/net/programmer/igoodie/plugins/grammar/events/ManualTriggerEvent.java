package net.programmer.igoodie.plugins.grammar.events;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.util.Couple;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ManualTriggerEvent extends TSLEvent {

    public static final ManualTriggerEvent INSTANCE = new ManualTriggerEvent();

    private ManualTriggerEvent() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "Manual Trigger");
    }

    @Override
    public @NotNull Map<String, Class<?>> getAcceptedFields() {
        return eventFields(
                new Couple<>("debug_data", GoodieObject.class)
        );
    }

}
