package coconut.plugin;

import net.programmer.igoodie.legacy.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;

public class CoconutPlugin extends TSLPlugin {

    @TSLPluginInstance
    public static CoconutPlugin INSTANCE;

    protected CoconutPlugin(TSLPluginContext pluginContext) {
        super(pluginContext);
    }

//    public CoconutPlugin() {
//        super(new TSLPluginManifest(
//                "coconutplugin",
//                "Coconut Orange's Utilities",
//                "0.0.1",
//                "iGoodie"
//        ));
//    }

//    @Override
//    public void registerEvents(TSLRegistry<TSLEvent> registry) {
//        registry.register(CoconutFeastEvent.INSTANCE);
//    }

}
