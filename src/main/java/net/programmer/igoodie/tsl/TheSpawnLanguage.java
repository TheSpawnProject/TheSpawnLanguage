package net.programmer.igoodie.tsl;

import com.vdurmont.semver4j.Semver;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.plugins.events.common.CommonEvents;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.library.TSLUtilitiesLibrary;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLPluginLoadingException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class TheSpawnLanguage {

    public static final String TSL_VERSION = "0.0.0";
    public static final Semver TSL_SEMVER = new Semver(TSL_VERSION, Semver.SemverType.NPM);

    public final Set<String> LOADED_PLUGIN_IDS;
    public final Set<TSLPlugin> LOADED_PLUGINS;

    public final TSLRegistry<TSLTag> TAG_REGISTRY;
    public final TSLRegistry<TSLDecorator> DECORATOR_REGISTRY;
    public final TSLRegistry<TSLEvent> EVENT_REGISTRY;
    public final TSLRegistry<TSLAction> ACTION_REGISTRY;
    public final TSLRegistry<TSLPredicate> PREDICATE_REGISTRY;
    public final TSLRegistry<TSLComparator> COMPARATOR_REGISTRY;
    public final TSLRegistry<TSLFunction> FUNCTION_REGISTRY;

    protected final JSEngine jsEngine;

    public TheSpawnLanguage() {
        LOADED_PLUGIN_IDS = new HashSet<>();
        LOADED_PLUGINS = new HashSet<>();

        TAG_REGISTRY = new TSLRegistry<>(StringUtilities::upperSnake);
        DECORATOR_REGISTRY = new TSLRegistry<>();
        EVENT_REGISTRY = new TSLRegistry<>(StringUtilities::upperFirstLetters);
        ACTION_REGISTRY = new TSLRegistry<>(StringUtilities::allUpper);
        PREDICATE_REGISTRY = TSLRegistry.createWithCapacity(2);
        COMPARATOR_REGISTRY = new TSLRegistry<>(StringUtilities::allUpper);
        FUNCTION_REGISTRY = new TSLRegistry<TSLFunction>() {
            @Override
            public void postRegister(TSLFunction function) {
                jsEngine.loadFunction(function);
            }
        };

        jsEngine = new JSEngine();

        loadBuiltInPackages();
    }

    public JSEngine getJsEngine() {
        return jsEngine;
    }

    private void loadBuiltInPackages() {
        jsEngine.defineConst("$TSL_VERSION", TSL_VERSION);
        jsEngine.loadLibrary(new TSLUtilitiesLibrary());
        loadPlugin(new TSLGrammarCore());
        loadPlugin(new CommonEvents());
    }

    public void loadPlugin(TSLPlugin plugin) {
        loadPlugin(plugin, null);
    }

    public void loadPlugin(TSLPlugin plugin, String filePath) {
        TSLPluginManifest manifest = plugin.getManifest();
        String pluginId = manifest.getPluginId();

        if (LOADED_PLUGIN_IDS.contains(pluginId)) {
            throw new TSLPluginLoadingException("Plugin already has loaded in (id = " + pluginId + ")", filePath);
        }

        assignAnnotatedFields(plugin);

        plugin.setLanguage(this);
        plugin.registerTags(TAG_REGISTRY);
        plugin.registerDecorators(DECORATOR_REGISTRY);
        plugin.registerEvents(EVENT_REGISTRY);
        plugin.registerActions(ACTION_REGISTRY);
        plugin.registerPredicates(PREDICATE_REGISTRY);
        plugin.registerComparators(COMPARATOR_REGISTRY);
        plugin.registerFunctions(FUNCTION_REGISTRY);
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
