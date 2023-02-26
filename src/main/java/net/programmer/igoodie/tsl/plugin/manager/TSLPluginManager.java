package net.programmer.igoodie.tsl.plugin.manager;

import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.logging.TSLLogHandler;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.plugin.TSLCorePlugin;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginLogger;
import org.pf4j.*;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TSLPluginManager extends DefaultPluginManager {

    protected TheSpawnLanguage tsl;
    protected Set<String> corePluginIds;

    public TSLPluginManager(TheSpawnLanguage tsl, List<Path> rootPaths, List<Class<? extends TSLCorePlugin>> corePluginClasses) {
        super(rootPaths);

        this.tsl = tsl;
        this.corePluginIds = new HashSet<>();

        for (Class<? extends TSLCorePlugin> corePluginClass : corePluginClasses) {
            try {
                TSLCorePlugin corePlugin = corePluginClass.newInstance();
                this.corePluginIds.add(corePlugin.getDescriptor().getPluginId());
                loadCorePlugin(corePlugin);

            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public TheSpawnLanguage getTsl() {
        return tsl;
    }

    public List<TSLCorePlugin> getCorePlugins() {
        return corePluginIds.stream()
                .map(this::getPlugin)
                .map(PluginWrapper::getPlugin)
                .filter(plugin -> plugin instanceof TSLCorePlugin)
                .map(plugin -> ((TSLCorePlugin) plugin))
                .collect(Collectors.toList());
    }

    public boolean hasLoadedCorePlugin(TSLPlugin plugin) {
        return this.corePluginIds.contains(plugin.getDescriptor().getPluginId());
    }

    /* ---------------------- */

    @Override
    protected PluginFactory createPluginFactory() {
        return new TSLPluginFactory();
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return new SingletonExtensionFactory(this);
    }

    /* ---------------------- */

    @Override
    protected PluginWrapper loadPluginFromPath(Path pluginPath) {
        PluginWrapper pluginWrapper = super.loadPluginFromPath(pluginPath);

        Plugin plugin = pluginWrapper.getPlugin();
        if (plugin instanceof TSLPlugin) {
            TSLPlugin tslPlugin = (TSLPlugin) plugin;
            assignAnnotatedFields(tslPlugin);
        }

        return pluginWrapper;
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
    protected PluginState stopPlugin(String pluginId, boolean stopDependents) {
        if (corePluginIds.contains(pluginId))
            throw new IllegalArgumentException("Cannot stop Core Plugins.");
        return super.stopPlugin(pluginId, stopDependents);
    }

    @Override
    protected boolean unloadPlugin(String pluginId, boolean unloadDependents) {
        if (corePluginIds.contains(pluginId))
            throw new IllegalArgumentException("Cannot unload Core Plugins.");
        return super.unloadPlugin(pluginId, unloadDependents);
    }

    protected final void loadCorePlugin(TSLCorePlugin tslPlugin) {
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
        getResolvedPlugins().add(pluginWrapper);

        // add tslPlugin class loader to the list with class loaders
        getPluginClassLoaders().put(pluginId, pluginClassLoader);
    }

    protected final void assignAnnotatedFields(TSLPlugin plugin) {
        for (Field field : plugin.getClass().getFields()) {
            if (field.isAnnotationPresent(TSLPluginInstance.class)) {
                ReflectionUtilities.setValue(null, field, plugin);

            } else if (field.isAnnotationPresent(TSLPluginLogger.class)) {
                TSLLogHandler logHandler = new TSLLogHandler
                        (tsl.getLogsPath().resolve("plugins").toFile(), plugin.getDescriptor().getPluginId())
                        .historyLimit(5)
                        .hookConsoleLog();
                TSLLogger logger = TSLLogger.createLogger(plugin, logHandler);
                ReflectionUtilities.setValue(null, field, logger);
            }
        }
    }

    /* ---------------------- */

    public static class Builder {

        protected TheSpawnLanguage tsl;
        protected List<Path> pluginPaths = new LinkedList<>();
        protected List<Class<? extends TSLCorePlugin>> corePluginClasses = new LinkedList<>();

        private Builder() {}

        public Builder pluginPath(Path pluginPath) {
            this.pluginPaths.add(pluginPath);
            return this;
        }

        public Builder pluginPaths(List<Path> pluginPaths) {
            this.pluginPaths.addAll(pluginPaths);
            return this;
        }

        public Builder corePlugin(Class<? extends TSLCorePlugin> corePluginClass) {
            this.corePluginClasses.add(corePluginClass);
            return this;
        }

        public Builder corePlugins(List<Class<? extends TSLCorePlugin>> corePluginClasses) {
            this.corePluginClasses.addAll(corePluginClasses);
            return this;
        }

        public TSLPluginManager build() {
            TSLPluginManager manager = new TSLPluginManager(tsl, pluginPaths, corePluginClasses);
            manager.setSystemVersion(TheSpawnLanguage.TSL_VERSION);
            return manager;
        }

        public static Builder forTSL(TheSpawnLanguage tsl) {
            Builder builder = new Builder();
            builder.tsl = tsl;
            return builder;
        }

    }

}
