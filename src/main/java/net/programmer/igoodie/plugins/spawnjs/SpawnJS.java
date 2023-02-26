package net.programmer.igoodie.plugins.spawnjs;

import net.programmer.igoodie.legacy.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;

public class SpawnJS extends TSLPlugin {

    private static final String VERSION = "0.0.1-alpha";

    @TSLPluginInstance
    public static SpawnJS PLUGIN_INSTANCE;

    protected SpawnJS(TSLPluginContext pluginContext) {
        super(pluginContext);
    }

//    public SpawnJS() {
//        super(new TSLPluginManifest(
//                "spawnjs",
//                "SpawnJS",
//                VERSION,
//                "iGoodie"
//        ));
//    }

//    @Override
//    public void registerFunctionLibraries(TSLRegistry<TSLFunctionLibrary> registry) {
//        registry.register(SpawnJSCorelib.INSTANCE);
//        registry.register(SpawnJSFuncLib.INSTANCE);
//    }

}
