package example.plugin;

import net.programmer.igoodie.tsl.definition.base.TSLAttribute;

public class ExampleAttributes {

    public static final TSLAttribute<Boolean> NOTIFICATIONS_SUPPRESSED = new TSLAttribute<>(ExamplePlugin.PLUGIN_INSTANCE, "notifications_suppressed");
    public static final TSLAttribute<Double> COOLDOWN_DURATION = new TSLAttribute<>(ExamplePlugin.PLUGIN_INSTANCE, "cooldown_duration");

}
