package net.programmer.igoodie.plugins.events.common;

import net.programmer.igoodie.legacy.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;

public class CommonEvents extends TSLPlugin {

    private static final String VERSION = "1.0.0";

    @TSLPluginInstance
    public static CommonEvents PLUGIN_INSTANCE;

    protected CommonEvents(TSLPluginContext pluginContext) {
        super(pluginContext);
    }

//    public CommonEvents() {
//        super(new TSLPluginManifest(
//                "common_events",
//                "Common Events",
//                VERSION,
//                "iGoodie"
//        ));
//    }

//    @Override
//    public void registerEvents(TSLRegistry<TSLEvent> registry) {
//        registry.register(DonationEvent.INSTANCE);
//    }

}
