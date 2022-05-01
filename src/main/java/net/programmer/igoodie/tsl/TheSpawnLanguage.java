package net.programmer.igoodie.tsl;

import com.vdurmont.semver4j.Semver;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.plugins.events.common.CommonEvents;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.spawnjs.SpawnJS;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLImplementationError;
import net.programmer.igoodie.tsl.exception.TSLImportError;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.TSLFunctionsCorelib;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginManager;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.tsl.runtime.TSLReservedNames;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TheSpawnLanguage {

    public static final String TSL_VERSION = "0.0.0";
    public static final Semver TSL_SEMVER = new Semver(TSL_VERSION, Semver.SemverType.NPM);

    private final List<TSLPlugin> BUILT_IN_PLUGINS;
    private final TSLReservedNames RESERVED_NAMES;

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

    private Function<String, String> keyMapper(Function<String, String> mapper) {
        return (key) -> {
            int colonCount = StringUtils.occurrenceCount(key, ':');
            if (colonCount > 1) {
                throw new TSLImplementationError("Registry keys MUST not have multiple colon characters");
            }
            System.out.print(key + " -> ");
            String[] parts = key.split(":");
            String namespace = parts[0];
            String value = parts[1];
            System.out.println(namespace + ":" + mapper.apply(value));
            return namespace + ":" + mapper.apply(value);
        };
    }

    public TheSpawnLanguage() {
        BUILT_IN_PLUGINS = new LinkedList<>();
        RESERVED_NAMES = new TSLReservedNames();
        TAG_REGISTRY = new TSLRegistry<>(keyMapper(StringUtilities::upperSnake));
        DECORATOR_REGISTRY = new TSLRegistry<>();
        EVENT_REGISTRY = new TSLRegistry<>(keyMapper(StringUtilities::upperFirstLetters));
        ACTION_REGISTRY = new TSLRegistry<>(keyMapper(StringUtilities::allUpper));
        PREDICATE_REGISTRY = TSLRegistry.createWithCapacity(2);
        COMPARATOR_REGISTRY = new TSLRegistry<>(keyMapper(StringUtilities::allUpper));
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
        BUILT_IN_PLUGINS.add(new TSLGrammarCore());
        BUILT_IN_PLUGINS.add(new SpawnJS());
        BUILT_IN_PLUGINS.add(new CommonEvents());
        BUILT_IN_PLUGINS.forEach(pluginManager::loadPlugin);

        for (Map.Entry<String, TSLFunctionsCorelib> corelibEntry : FUNC_CORELIB_REGISTRY) {
            TSLFunctionsCorelib corelib = corelibEntry.getValue();
            jsEngine.loadCoreLibrary(corelib);
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

    /* ------------------------ */

    @Nullable
    public TSLTag getTag(TSLRuleset ruleset, TSLPlainWord tagNameToken) {
        String namespace = tagNameToken.getNamespace();
        String value = tagNameToken.getValue();

        if (namespace == null) {
            return getDefinition(TAG_REGISTRY, value);
        }

        String pluginId = ruleset.getImportedPlugins().get(namespace);

        if (pluginId == null) {
            throw new TSLImportError("Could not resolve " + namespace, tagNameToken);
        }

        return getDefinition(TAG_REGISTRY, pluginId + ":" + value);
    }

    @Nullable
    public TSLDecorator getDecorator(TSLRuleset ruleset, TSLDecoratorCall decoratorCallToken) {
        String namespace = decoratorCallToken.getNamespace();
        String value = decoratorCallToken.getName();

        if (namespace == null) {
            return getDefinition(DECORATOR_REGISTRY, value);
        }

        String pluginId = ruleset.getImportedPlugins().get(namespace);

        if (pluginId == null) {
            throw new TSLImportError("Could not resolve " + namespace, decoratorCallToken);
        }

        return getDefinition(DECORATOR_REGISTRY, pluginId + ":" + value);
    }

    @Nullable
    public TSLEvent getEvent(TSLRuleset ruleset, List<TSLPlainWord> eventTokens) {
        String eventExpression = eventTokens.stream()
                .map(TSLPlainWord::getRaw)
                .collect(Collectors.joining(" "));

        Pattern eventPattern = Pattern.compile("(.*) FROM (.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = eventPattern.matcher(eventExpression);

        if (matcher.matches()) {
            String eventName = matcher.group(1);
            String namespace = matcher.group(2);

            String pluginId = ruleset.getImportedPlugins().get(namespace);

            if (pluginId == null) {
                TSLPlainWord namespaceToken = eventTokens.get(eventTokens.size() - 1);
                throw new TSLImportError("Could not resolve " + namespace, namespaceToken);
            }

            TSLEvent definition = getDefinition(EVENT_REGISTRY, pluginId + ":" + eventName);

            if (definition != null) {
                return definition;
            }
        }

        return getDefinition(EVENT_REGISTRY, eventExpression);
    }

    @Nullable
    public TSLAction getAction(TSLRuleset ruleset, TSLPlainWord actionName) {
        String namespace = actionName.getNamespace();
        String value = actionName.getValue();

        if (namespace == null) {
            return getDefinition(ACTION_REGISTRY, value);
        }

        String pluginId = ruleset.getImportedPlugins().get(namespace);

        if (pluginId == null) {
            throw new TSLImportError("Could not resolve " + namespace, actionName);
        }

        return getDefinition(ACTION_REGISTRY, pluginId + ":" + value);

    }

    @Nullable
    public TSLPredicate getPredicate(String id) {
        return getDefinition(PREDICATE_REGISTRY, id);
    }

    @Nullable
    public TSLComparator getComparator(String id) {
        return getDefinition(COMPARATOR_REGISTRY, id);
    }

    @Nullable
    public TSLFunctionLibrary getFunctionLibrary(String id) {
        return getDefinition(FUNC_LIBRARY_REGISTRY, id);
    }

    @Nullable
    public <T extends TSLDefinition> T getDefinition(TSLRegistry<T> registry, String id) {
        System.out.println("\nGet Definition; " + id);
        if (!id.contains(":")) {
            for (TSLPlugin builtInPlugin : BUILT_IN_PLUGINS) {
                String pluginId = builtInPlugin.getManifest().getPluginId();
                if (registry.has(pluginId + ":" + id)) {
                    return registry.get(pluginId + ":" + id);
                }
            }
            return null;
        }
        return registry.get(id);
    }

}
