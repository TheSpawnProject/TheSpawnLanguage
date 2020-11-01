package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.ActionRegistry;
import net.programmer.igoodie.tsl.registry.EventFieldRegistry;
import net.programmer.igoodie.tsl.registry.EventRegistry;
import net.programmer.igoodie.tsl.registry.FunctionRegistry;

import java.util.HashSet;
import java.util.Set;

public class TheSpawnLanguage {

    public final Set<String> LOADED_PLUGINS;

    public final EventRegistry EVENT_REGISTRY;
    public final EventFieldRegistry EVENT_FIELD_REGISTRY;
    public final ActionRegistry ACTION_REGISTRY;
    public final FunctionRegistry FUNCTION_REGISTRY;

    protected final JSEngine jsEngine;

    public TheSpawnLanguage() {
        LOADED_PLUGINS = new HashSet<>();

        EVENT_REGISTRY = new EventRegistry();
        EVENT_FIELD_REGISTRY = new EventFieldRegistry();
        ACTION_REGISTRY = new ActionRegistry();
        FUNCTION_REGISTRY = new FunctionRegistry();

        jsEngine = new JSEngine(FUNCTION_REGISTRY);
        jsEngine.putGlobalBinding("TSL_VERSION", "0.0.0");
    }

    public JSEngine getJsEngine() {
        return jsEngine;
    }

    public void loadPlugin(TSLPlugin plugin) {
        TSLPluginManifest manifest = plugin.getManifest();
        String pluginId = manifest.getName() + ":" + manifest.getVersion();

        if (LOADED_PLUGINS.contains(pluginId))
            return; // TODO: What to do here? Consider

        plugin.registerEvents(EVENT_REGISTRY);
        plugin.registerEventFields(EVENT_FIELD_REGISTRY);
        plugin.registerActions(ACTION_REGISTRY);
        plugin.registerFunctions(FUNCTION_REGISTRY);
        LOADED_PLUGINS.add(pluginId);
    }

}
