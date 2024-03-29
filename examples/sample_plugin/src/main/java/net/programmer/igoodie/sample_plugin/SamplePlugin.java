package net.programmer.igoodie.sample_plugin;

import net.programmer.igoodie.sample_plugin.actions.PrintAction;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.util.ValueHolder;
import org.pf4j.ExtensionPoint;

import java.util.List;

public class SamplePlugin extends TSLPlugin {

    @TSLPluginInstance
    public static SamplePlugin PLUGIN_INSTANCE;

    @TSLPluginLogger
    public static TSLLogger LOGGER;

    public SamplePlugin(TSLPluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public List<ValueHolder<TSLAction>> getActions() {
        return createDefinitionList(
                PrintAction.INSTANCE
        );
    }

}
