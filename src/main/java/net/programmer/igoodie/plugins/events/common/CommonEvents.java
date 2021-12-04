package net.programmer.igoodie.plugins.events.common;

import net.programmer.igoodie.plugins.events.common.events.DonationEvent;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

public class CommonEvents extends TSLPlugin {

    private static final String VERSION = "1.0.0";

    @TSLPluginInstance
    public static CommonEvents PLUGIN_INSTANCE;

    public CommonEvents() {
        super(new TSLPluginManifest(
                "common_events",
                "Common Events",
                VERSION,
                "iGoodie"
        ));
    }

    @Override
    public void registerEvents(TSLRegistry<TSLEvent> registry) {
        registry.register(DonationEvent.INSTANCE);
    }

}
