package net.programmer.igoodie.tsl.plugin.manager;

import org.pf4j.DefaultPluginFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.lang.reflect.Constructor;

public class TSLPluginFactory extends DefaultPluginFactory {

    @Override
    protected Plugin createInstance(Class<?> pluginClass, PluginWrapper pluginWrapper) {
        TSLPluginContext tslPluginContext = new TSLPluginContext(pluginWrapper);

        try {
            Constructor<?> constructor = pluginClass.getConstructor(TSLPluginContext.class);
            return (Plugin) constructor.newInstance(tslPluginContext);

        } catch (Exception ignored) {}

        return super.createInstance(pluginClass, pluginWrapper);
    }

}
