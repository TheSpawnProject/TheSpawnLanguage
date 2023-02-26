package net.programmer.igoodie.tsl.plugin.manager;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.legacy.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.plugin.TSLCorePlugin;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginDescriptor;
import net.programmer.igoodie.tsl.plugin.extension.TSLDefinitionsEP;
import org.pf4j.*;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TSLPluginManager extends DefaultPluginManager {

    protected TheSpawnLanguage tsl;
    protected Set<String> corePluginIds;

    public TSLPluginManager(TheSpawnLanguage tsl, List<Couple<TSLPluginDescriptor, Class<? extends TSLCorePlugin>>> corePlugins) {
        super();

        this.tsl = tsl;
        this.corePluginIds = new HashSet<>();

//        for (Couple<TSLPluginDescriptor, Class<? extends TSLCorePlugin>> corePlugin : corePlugins) {
//            loadCorePlugin(corePlugin.getFirst(), corePlugin.getSecond());
//        }
    }

    @Override
    protected PluginFactory createPluginFactory() {
        return new TSLPluginFactory();
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return new SingletonExtensionFactory(this);
    }

    @Override
    public String loadPlugin(Path pluginPath) {
        String pluginId = super.loadPlugin(pluginPath);

        Plugin plugin = getPlugin(pluginId).getPlugin();
        if (plugin instanceof TSLPlugin) {
            TSLPlugin tslPlugin = (TSLPlugin) plugin;
            assignAnnotatedFields(tslPlugin);
        }

        return pluginId;
    }

    @Override
    public PluginState startPlugin(String pluginId) {
        PluginState state = super.startPlugin(pluginId);

        System.out.println("Starting " + pluginId);

        Plugin plugin = getPlugin(pluginId).getPlugin();
        if (plugin instanceof TSLDefinitionsEP) {
            TSLDefinitionsEP definitions = (TSLDefinitionsEP) plugin;
            definitions.registerDefinitions(this.tsl);
        }

        return state;
    }

    @Override
    protected PluginState stopPlugin(String pluginId, boolean stopDependents) {
        PluginState state = super.stopPlugin(pluginId, stopDependents);

        Plugin plugin = getPlugin(pluginId).getPlugin();
        if (plugin instanceof TSLDefinitionsEP) {
            TSLDefinitionsEP definitions = (TSLDefinitionsEP) plugin;
            definitions.unregisterDefinitions(this.tsl);
        }

        return state;
    }

    // TODO: Make used only on the constructor.
    public void loadCorePlugin(TSLCorePlugin tslPlugin) {
        assignAnnotatedFields(tslPlugin);

        PluginDescriptor descriptor = tslPlugin.getDescriptor();
        String pluginId = descriptor.getPluginId();

        validatePluginDescriptor(descriptor);

        if (plugins.containsKey(pluginId)) {
            PluginWrapper loadedPlugin = getPlugin(pluginId);
            throw new PluginRuntimeException("There is an already loaded tslPlugin ({}) "
                    + "with the same id ({}). Simultaneous loading "
                    + "of plugins with the same PluginId is not currently supported.\n"
                    + "As a workaround you may include PluginVersion and PluginProvider "
                    + "in PluginId.",
                    loadedPlugin, pluginId);
        }

        String pluginClass = descriptor.getPluginClass();
        ClassLoader pluginClassLoader = tslPlugin.getClass().getClassLoader();

        PluginWrapper pluginWrapper = createPluginWrapper(descriptor,
                new File(pluginId).toPath(), pluginClassLoader);
        pluginWrapper.setPluginFactory(wrapper -> {
            tslPlugin.setPluginContext(new TSLPluginContext(wrapper));
            return tslPlugin;
        });

        // test for disabled tslPlugin
        if (isPluginDisabled(descriptor.getPluginId())) {
            pluginWrapper.setPluginState(PluginState.DISABLED);
        }

        // validate the tslPlugin
        if (!isPluginValid(pluginWrapper)) {
            pluginWrapper.setPluginState(PluginState.DISABLED);
        }

        // add tslPlugin to the list with plugins
        plugins.put(pluginId, pluginWrapper);
        getUnresolvedPlugins().add(pluginWrapper);

        // add tslPlugin class loader to the list with class loaders
        getPluginClassLoaders().put(pluginId, pluginClassLoader);
    }

    private void assignAnnotatedFields(TSLPlugin plugin) {
        for (Field field : plugin.getClass().getFields()) {
            if (field.isAnnotationPresent(TSLPluginInstance.class)) {
                ReflectionUtilities.setValue(null, field, plugin);

            }
//            else if (field.isAnnotationPresent(TSLPluginLogger.class)) {
//                TSLLogHandler logHandler = new TSLLogHandler
//                        (new File("logs/plugins"), plugin.getManifest().getPluginId())
//                        .historyLimit(5)
//                        .hookConsoleLog();
//                TSLLogger logger = TSLLogger.createLogger(plugin, logHandler);
//                ReflectionUtilities.setValue(null, field, logger);
//            }
        }
    }

}
