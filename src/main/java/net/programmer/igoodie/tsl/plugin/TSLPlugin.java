package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.plugin.extension.TSLDefinitionsEP;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginManager;
import org.pf4j.Plugin;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;

public class TSLPlugin extends Plugin implements TSLDefinitionsEP {

    protected TSLPluginContext pluginContext;

    public TSLPlugin(TSLPluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    public PluginDescriptor getDescriptor() {
        return pluginContext.getWrapper().getDescriptor();
    }

    /* ----------------- */

    @Override
    public final void start() {
        super.start();

        PluginManager pluginManager = pluginContext.getWrapper().getPluginManager();
        if (pluginManager instanceof TSLPluginManager) {
            TheSpawnLanguage tsl = ((TSLPluginManager) pluginManager).getTsl();
            registerDefinitions(tsl);
        }

        onStarted();
    }

    protected void onStarted() {}

    @Override
    public final void stop() {
        super.stop();

        PluginManager pluginManager = pluginContext.getWrapper().getPluginManager();
        if (pluginManager instanceof TSLPluginManager) {
            TheSpawnLanguage tsl = ((TSLPluginManager) pluginManager).getTsl();
            unregisterDefinitions(tsl);
        }

        onStopped();
    }

    protected void onStopped() {}

}
