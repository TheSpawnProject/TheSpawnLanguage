package example.plugin;

import net.programmer.igoodie.tsl.runtime.attribute.TSLAttribute;

public class ExampleAttributes {

    public static final TSLAttribute<Boolean> NOTIFICATIONS_SUPPRESSED = new TSLAttribute<>(ExamplePlugin.PLUGIN_INSTANCE, "notifications_suppressed",
            goodieElement -> goodieElement.asPrimitive().getBoolean());
    public static final TSLAttribute<Double> COOLDOWN_DURATION = new TSLAttribute<>(ExamplePlugin.PLUGIN_INSTANCE, "cooldown_duration",
            goodieElement -> goodieElement.asPrimitive().getDouble());

}
