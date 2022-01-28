package net.programmer.igoodie.tsl.plugin;

import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.exception.TSLPluginLoadingException;
import net.programmer.igoodie.tsl.logging.TSLLogger;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class TSLPluginManager {

    public static final Map<String, TSLPlugin> LOADED_PATHS = new HashMap<>();

    private final TheSpawnLanguage language;

    public final Set<TSLPlugin> LOADED_PLUGINS;
    public final Set<String> LOADED_PLUGIN_IDS;

    public TSLPluginManager(TheSpawnLanguage language) {
        this.language = language;
        LOADED_PLUGIN_IDS = new HashSet<>();
        LOADED_PLUGINS = new HashSet<>();
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

        plugin.registerTags(language.TAG_REGISTRY);
        plugin.registerDecorators(language.DECORATOR_REGISTRY);
        plugin.registerEvents(language.EVENT_REGISTRY);
        plugin.registerActions(language.ACTION_REGISTRY);
        plugin.registerPredicates(language.PREDICATE_REGISTRY);
        plugin.registerComparators(language.COMPARATOR_REGISTRY);
        plugin.registerFunctions(language.FUNCTION_REGISTRY);
        LOADED_PLUGIN_IDS.add(pluginId);
        LOADED_PLUGINS.add(plugin);
    }

    private void assignAnnotatedFields(TSLPlugin plugin) {
        for (Field field : plugin.getClass().getFields()) {
            if (field.isAnnotationPresent(TSLPluginInstance.class)) {
                ReflectionUtilities.setValue(null, field, plugin);

            } else if (field.isAnnotationPresent(TSLPluginLogger.class)) {
                Logger logger = TSLLogger.createLogger(new File("logs"), plugin, true);
                ReflectionUtilities.setValue(null, field, logger);
            }
        }
    }

}
