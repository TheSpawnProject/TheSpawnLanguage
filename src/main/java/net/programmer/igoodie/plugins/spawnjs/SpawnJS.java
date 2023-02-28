package net.programmer.igoodie.plugins.spawnjs;

import net.programmer.igoodie.plugins.spawnjs.corelib.SpawnJSCorelib;
import net.programmer.igoodie.plugins.spawnjs.funclib.SpawnJSFuncLib;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.plugin.TSLCorePlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginDescriptor;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginInstance;
import net.programmer.igoodie.tsl.util.ValueHolder;
import org.pf4j.PluginDescriptor;

import java.util.List;

public class SpawnJS extends TSLCorePlugin {

    public static final TSLPluginDescriptor DESCRIPTOR = createCoreDescriptor(
            "*",
            "spawnjs",
            "SpawnJS",
            "TODO: Description here",
            "0.0.1-alpha",
            "iGoodie",
            "MIT"
    );

    @TSLPluginInstance
    public static SpawnJS PLUGIN_INSTANCE;

    @Override
    public PluginDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public List<ValueHolder<TSLFunctionLibrary>> getFunctionLibraries() {
        return createDefinitionList(
                SpawnJSCorelib.INSTANCE,
                SpawnJSFuncLib.INSTANCE
        );
    }

}
