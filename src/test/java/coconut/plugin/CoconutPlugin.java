package coconut.plugin;

import coconut.plugin.event.CoconutFeastEvent;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

public class CoconutPlugin extends TSLPlugin {

    @TSLPluginInstance
    public static CoconutPlugin INSTANCE;

    public CoconutPlugin() {
        super(new TSLPluginManifest(
                "coconutplugin",
                "Coconut Orange's Utilities",
                "0.0.1",
                "iGoodie"
        ));
    }

    @Override
    public void registerEvents(TSLRegistry<TSLEvent> registry) {
        registry.register(CoconutFeastEvent.INSTANCE);
    }

}
