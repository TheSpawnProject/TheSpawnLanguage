package net.programmer.igoodie.plugins.events.common;

import net.programmer.igoodie.plugins.events.common.events.DonationEvent;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.jar.Attributes;

public class CommonEvents extends TSLPlugin {

    private static final String VERSION = "1.0.0";

    @TSLPluginInstance
    public static CommonEvents PLUGIN_INSTANCE;

    public CommonEvents() {
        try {
            Field manifestField = TSLPlugin.class.getDeclaredField("manifest");
            TSLPluginManifest manifest = new TSLPluginManifest(getJarAttributes());
            ReflectionUtilities.setValue(this, manifestField, manifest);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new InternalError();
        }
    }

    private Attributes getJarAttributes() {
        Attributes attributes = new Attributes();
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_ID, "common_events");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_NAME, "Common Events");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_VERSION, VERSION);
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_AUTHOR, "iGoodie");
        return attributes;
    }

    @Override
    public void registerEvents(TSLRegistry<TSLEvent> registry) {
        registry.register(DonationEvent.INSTANCE);
    }

}
