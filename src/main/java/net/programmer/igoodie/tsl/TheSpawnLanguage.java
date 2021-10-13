package net.programmer.igoodie.tsl;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.library.TSLUtilitiesLibrary;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class TheSpawnLanguage {

    public final Set<String> LOADED_PLUGINS;

    public final TSLRegistry<TSLTag> TAG_REGISTRY;
    public final TSLRegistry<TSLDecorator> DECORATOR_REGISTRY;
    public final TSLRegistry<TSLEvent> EVENT_REGISTRY;
    public final TSLRegistry<TSLEventField<?>> EVENT_FIELD_REGISTRY;
    public final TSLRegistry<TSLAction> ACTION_REGISTRY;
    public final TSLRegistry<TSLPredicate> PREDICATE_REGISTRY;
    public final TSLRegistry<TSLComparator> COMPARATOR_REGISTRY;
    public final TSLRegistry<TSLFunction> FUNCTION_REGISTRY;

    protected final JSEngine jsEngine;

    public TheSpawnLanguage() {
        LOADED_PLUGINS = new HashSet<>();

        TAG_REGISTRY = new TSLRegistry<>(StringUtils::upperSnake);
        DECORATOR_REGISTRY = new TSLRegistry<>();
        EVENT_REGISTRY = new TSLRegistry<>(StringUtils::upperFirstLetters);
        EVENT_FIELD_REGISTRY = new TSLRegistry<>();
        ACTION_REGISTRY = new TSLRegistry<>(StringUtils::allUpper);
        PREDICATE_REGISTRY = TSLRegistry.createWithCapacity(2);
        COMPARATOR_REGISTRY = new TSLRegistry<>(StringUtils::allUpper);
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
        jsEngine.defineConst("$TSL_VERSION", "0.0.0");
        jsEngine.loadLibrary(new TSLUtilitiesLibrary());
        loadPlugin(new TSLGrammarCore());
    }

    public void loadPlugin(TSLPlugin plugin) {
        TSLPluginManifest manifest = plugin.getManifest();
        String pluginId = manifest.getName() + ":" + manifest.getVersion();

        // TODO: Disallow loading of same plugin but different versions

        if (LOADED_PLUGINS.contains(pluginId))
            return; // TODO: What to do here? Consider

        assignAnnotatedFields(plugin);

        plugin.setLanguage(this);
        plugin.registerTags(TAG_REGISTRY);
        plugin.registerDecorators(DECORATOR_REGISTRY);
        plugin.registerEvents(EVENT_REGISTRY);
        plugin.registerEventFields(EVENT_FIELD_REGISTRY);
        plugin.registerActions(ACTION_REGISTRY);
        plugin.registerPredicates(PREDICATE_REGISTRY);
        plugin.registerComparators(COMPARATOR_REGISTRY);
        plugin.registerFunctions(FUNCTION_REGISTRY);
        LOADED_PLUGINS.add(pluginId);
    }

    private void assignAnnotatedFields(TSLPlugin plugin) {
        for (Field field : plugin.getClass().getFields()) {
            if (field.isAnnotationPresent(TSLPluginInstance.class)) {
                try {
                    field.set(null, plugin);
                } catch (IllegalAccessException e) {
                    // TODO: Log properly
                    System.out.println("Plugin's instance field must be public.");
                    e.printStackTrace();
                }

            } else if (field.isAnnotationPresent(TSLPluginLogger.class)) {
                try {
                    field.set(null, TSLLogger.createLogger(new File("logs"), plugin, true));
                } catch (IllegalAccessException e) {
                    // TODO: Log properly
                    System.out.println("Plugin's logger field must be public.");
                    e.printStackTrace();
                }
            }
        }
    }

}
