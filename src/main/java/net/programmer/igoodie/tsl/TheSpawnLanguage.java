package net.programmer.igoodie.tsl;

import com.vdurmont.semver4j.Semver;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.plugins.events.common.CommonEvents;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.spawnjs.SpawnJS;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.TSLFunctionsCorelib;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.plugin.TSLPluginManager;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.io.File;
import java.util.Map;

public class TheSpawnLanguage {

    public static final String TSL_VERSION = "0.0.0";
    public static final Semver TSL_SEMVER = new Semver(TSL_VERSION, Semver.SemverType.NPM);

    public final TSLRegistry<TSLTag> TAG_REGISTRY;
    public final TSLRegistry<TSLDecorator> DECORATOR_REGISTRY;
    public final TSLRegistry<TSLEvent> EVENT_REGISTRY;
    public final TSLRegistry<TSLAction> ACTION_REGISTRY;
    public final TSLRegistry<TSLPredicate> PREDICATE_REGISTRY;
    public final TSLRegistry<TSLComparator> COMPARATOR_REGISTRY;
    public final TSLRegistry<TSLFunctionLibrary> FUNC_LIBRARY_REGISTRY;
    private final TSLRegistry<TSLFunctionsCorelib> FUNC_CORELIB_REGISTRY;

    protected final JSEngine jsEngine;
    protected final TSLPluginManager pluginManager;

    public TheSpawnLanguage() {
        TAG_REGISTRY = new TSLRegistry<>(StringUtilities::upperSnake);
        DECORATOR_REGISTRY = new TSLRegistry<>();
        EVENT_REGISTRY = new TSLRegistry<>(StringUtilities::upperFirstLetters);
        ACTION_REGISTRY = new TSLRegistry<>(StringUtilities::allUpper);
        PREDICATE_REGISTRY = TSLRegistry.createWithCapacity(2);
        COMPARATOR_REGISTRY = new TSLRegistry<>(StringUtilities::allUpper);
        FUNC_CORELIB_REGISTRY = new TSLRegistry<>();
        FUNC_LIBRARY_REGISTRY = new TSLRegistry<TSLFunctionLibrary>() {
            @Override
            public void postRegister(TSLFunctionLibrary entry) {
                if (entry instanceof TSLFunctionsCorelib) {
                    TSLFunctionsCorelib corelib = (TSLFunctionsCorelib) entry;
                    FUNC_CORELIB_REGISTRY.register(corelib);
                }
            }
        };

        jsEngine = new JSEngine();
        jsEngine.defineConst("$TSL_VERSION", TSL_VERSION);

        pluginManager = new TSLPluginManager(this);
        pluginManager.loadPlugin(new TSLGrammarCore());
        pluginManager.loadPlugin(new SpawnJS());
        pluginManager.loadPlugin(new CommonEvents());

        for (Map.Entry<String, TSLFunctionsCorelib> corelibEntry : FUNC_CORELIB_REGISTRY) {
            jsEngine.loadCoreLibrary(corelibEntry.getValue());
        }
    }

    public JSEngine getJsEngine() {
        return jsEngine;
    }

    public TSLPluginManager getPluginManager() {
        return pluginManager;
    }

    public TSLRuleset loadRuleset(File file) {
        TSLParser parser = new TSLParser(this);
        return parser.parse(file);
    }

    public boolean perform(TSLRuleset ruleset, TSLEvent event, GoodieObject eventArguments) {
        TSLContext context = new TSLContext(this);
        context.setEvent(event);
        context.setEventArguments(eventArguments);
        return ruleset.perform(context);
    }

}
