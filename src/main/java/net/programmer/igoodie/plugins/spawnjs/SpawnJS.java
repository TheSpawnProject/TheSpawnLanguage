package net.programmer.igoodie.plugins.spawnjs;

import net.programmer.igoodie.plugins.spawnjs.corelib.SpawnJSCorelib;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

public class SpawnJS extends TSLPlugin {

    private static final String VERSION = "0.0.1-alpha";

    @TSLPluginInstance
    public static SpawnJS PLUGIN_INSTANCE;

    public SpawnJS() {
        super(new TSLPluginManifest(
                "spawnjs",
                "SpawnJS",
                VERSION,
                "iGoodie"
        ));
    }

    @Override
    public void registerFunctionLibraries(TSLRegistry<TSLFunctionLibrary> registry) {
        registry.register(SpawnJSCorelib.INSTANCE);
    }

}
