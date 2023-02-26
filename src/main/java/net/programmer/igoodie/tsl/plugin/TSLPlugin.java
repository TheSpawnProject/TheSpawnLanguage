package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.tsl.plugin.extension.TSLDefinitionsEP;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;
import org.pf4j.Plugin;
import org.pf4j.PluginDescriptor;

public class TSLPlugin extends Plugin implements TSLDefinitionsEP {

    protected TSLPluginContext pluginContext;

    public TSLPlugin(TSLPluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    public PluginDescriptor getDescriptor() {
        return pluginContext.getWrapper().getDescriptor();
    }

}
