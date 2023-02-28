package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;
import org.pf4j.PluginDescriptor;

import java.util.Collections;

public abstract class TSLCorePlugin extends TSLPlugin {

    public TSLCorePlugin() {
        super(null);
    }

    public void setPluginContext(TSLPluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    @Override
    public abstract PluginDescriptor getDescriptor();

    protected static TSLPluginDescriptor createCoreDescriptor(
            String targetPlatform,
            String pluginId,
            String pluginName,
            String pluginDescription,
            String pluginVersion,
            String pluginAuthor,
            String license
    ) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String pluginClass = ArrayAccessor.of(stackTrace).get(stackTrace.length - 1)
                .map(StackTraceElement::getClassName)
                .orElse("");
        return new TSLPluginDescriptor(
                targetPlatform,
                pluginClass,
                pluginId,
                pluginName,
                pluginDescription,
                pluginVersion,
                pluginAuthor,
                license,
                TheSpawnLanguage.TSL_VERSION, // Core Plugins shall always target system version, as they live in the system code
                Collections.emptyList()
        );
    }

}
