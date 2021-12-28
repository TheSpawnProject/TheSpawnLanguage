package net.programmer.igoodie.tsl;

import com.vdurmont.semver4j.Semver;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.plugins.events.common.CommonEvents;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.library.TSLUtilitiesLibrary;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.plugin.TSLPluginManager;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

public class TheSpawnLanguage {

    public static final String TSL_VERSION = "0.0.0";
    public static final Semver TSL_SEMVER = new Semver(TSL_VERSION, Semver.SemverType.NPM);

    public final TSLRegistry<TSLTag> TAG_REGISTRY;
    public final TSLRegistry<TSLDecorator> DECORATOR_REGISTRY;
    public final TSLRegistry<TSLEvent> EVENT_REGISTRY;
    public final TSLRegistry<TSLAction> ACTION_REGISTRY;
    public final TSLRegistry<TSLPredicate> PREDICATE_REGISTRY;
    public final TSLRegistry<TSLComparator> COMPARATOR_REGISTRY;
    public final TSLRegistry<TSLFunction> FUNCTION_REGISTRY;

    protected final JSEngine jsEngine;
    protected final TSLPluginManager pluginManager;

    public TheSpawnLanguage() {
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
        jsEngine.defineConst("$TSL_VERSION", TSL_VERSION);
        jsEngine.loadLibrary(new TSLUtilitiesLibrary());

        pluginManager = new TSLPluginManager(this);
        pluginManager.loadPlugin(new TSLGrammarCore());
        pluginManager.loadPlugin(new CommonEvents());
    }

    public JSEngine getJsEngine() {
        return jsEngine;
    }

    public TSLPluginManager getPluginManager() {
        return pluginManager;
    }

}
