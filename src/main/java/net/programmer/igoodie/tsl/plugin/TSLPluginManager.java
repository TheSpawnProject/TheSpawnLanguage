package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.exception.TSLPluginLoadingException;
import net.programmer.igoodie.tsl.logging.TSLLogHandler;
import net.programmer.igoodie.tsl.logging.TSLLogger;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

public class TSLPluginManager {

    public static final Map<String, TSLPlugin> LOADED_PATHS = new HashMap<>();

    protected final TheSpawnLanguage tsl;

    protected final Set<TSLPlugin> LOADED_PLUGINS;
    protected final Set<String> LOADED_PLUGIN_IDS;

    public TSLPluginManager(TheSpawnLanguage tsl) {
        this.tsl = tsl;
        LOADED_PLUGIN_IDS = new HashSet<>();
        LOADED_PLUGINS = new HashSet<>();
    }

    public Set<TSLPlugin> getLoadedPlugins() {
        return Collections.unmodifiableSet(LOADED_PLUGINS);
    }

    public Set<String> getLoadedPluginsIds() {
        return Collections.unmodifiableSet(LOADED_PLUGIN_IDS);
    }

    public void loadPlugin(URI uri) {
        String filePath = uri.getPath();

        // In order not to load from same file path
        if (LOADED_PATHS.containsKey(filePath)) {
            loadPlugin(LOADED_PATHS.get(filePath));
            return;
        }

        try {
            TSLPluginLoader loader = new TSLPluginLoader(uri);

            loader.loadManifest();
            TSLPluginManifest manifest = loader.getPluginManifest();

            // In order not to load the same plugin id
            if (LOADED_PLUGIN_IDS.contains(manifest.getPluginId())) {
                throw new TSLPluginLoadingException("Plugin already has loaded in")
                        .withManifest(manifest).withFilePath(filePath);
            }

            loader.load();
            TSLPlugin plugin = loader.getLoadedPlugin();

            loadPlugin(plugin);
            LOADED_PATHS.put(filePath, plugin);

        } catch (TSLPluginLoadingException e) {
            throw e.withFilePath(filePath);
        }
    }

    public void loadPlugin(TSLPlugin plugin) {
        TSLPluginManifest manifest = plugin.getManifest();
        String pluginId = manifest.getPluginId();

        if (LOADED_PLUGIN_IDS.contains(pluginId)) {
            throw new TSLPluginLoadingException("Plugin already has loaded in")
                    .withManifest(manifest);
        }

        assignAnnotatedFields(plugin);

        plugin.registerTags(tsl.TAG_REGISTRY);
        plugin.registerDecorators(tsl.DECORATOR_REGISTRY);
        plugin.registerEvents(tsl.EVENT_REGISTRY);
        plugin.registerActions(tsl.ACTION_REGISTRY);
        plugin.registerPredicates(tsl.PREDICATE_REGISTRY);
        plugin.registerComparators(tsl.COMPARATOR_REGISTRY);
        plugin.registerFunctionLibraries(tsl.FUNC_LIBRARY_REGISTRY);
        LOADED_PLUGIN_IDS.add(pluginId);
        LOADED_PLUGINS.add(plugin);
    }

    private void assignAnnotatedFields(TSLPlugin plugin) {
        for (Field field : plugin.getClass().getFields()) {
            if (field.isAnnotationPresent(TSLPluginInstance.class)) {
                ReflectionUtilities.setValue(null, field, plugin);

            } else if (field.isAnnotationPresent(TSLPluginLogger.class)) {
                TSLLogHandler logHandler = new TSLLogHandler(new File("logs/plugins"), plugin.getManifest().getPluginId())
                        .historyLimit(5)
                        .hookConsoleLog();
                TSLLogger logger = TSLLogger.createLogger(plugin, logHandler);
                ReflectionUtilities.setValue(null, field, logger);
            }
        }
    }

}
