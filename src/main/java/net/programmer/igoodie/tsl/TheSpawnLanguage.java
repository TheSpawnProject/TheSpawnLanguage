package net.programmer.igoodie.tsl;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.spawnjs.SpawnJS;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.eventqueue.TSLEventBuffer;
import net.programmer.igoodie.tsl.exception.TSLImplementationError;
import net.programmer.igoodie.tsl.exception.TSLImportError;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.TSLFunctionsCorelib;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.plugin.TSLCorePlugin;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginManager;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLReservedNames;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TheSpawnLanguage {

    public static final String TSL_VERSION = "0.0.1";

    private final String platform;
    private final TSLReservedNames RESERVED_NAMES;

    protected final Path logsPath;

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

    protected final TSLEventBuffer eventBuffer;

    private Function<String, String> keyMapper(Function<String, String> mapper) {
        return (key) -> {
            int colonCount = StringUtils.occurrenceCount(key, ':');
            if (colonCount > 1) {
                throw new TSLImplementationError("Registry keys MUST not have multiple colon characters");
            }
            String[] parts = key.split(":");
            String namespace = parts[0];
            String value = parts[1];
            return namespace + ":" + mapper.apply(value);
        };
    }

    @Deprecated()
    public TheSpawnLanguage() {
        this("System", Paths.get("logs"), Collections.emptyList(), Collections.emptyList());
    }

    private TheSpawnLanguage(String platform, Path logsPath, List<Path> pluginPaths, List<Class<? extends TSLCorePlugin>> corePluginClasses) {
        this.platform = platform;
        this.logsPath = logsPath;

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
                    if (pluginManager.hasLoadedCorePlugin(entry.getPlugin())) {
                        TheSpawnLanguage.this.registerCorelib(entry);
                    }
                }
            }
        };

        jsEngine = new JSEngine(this);
        jsEngine.defineConst("$TSL_VERSION", TSL_VERSION);

        pluginManager = TSLPluginManager.Builder.forTSL(this)
                .pluginPaths(pluginPaths)
                .corePlugin(TSLGrammarCore.class)
                .corePlugin(SpawnJS.class)
                .corePlugins(corePluginClasses)
                .build();

        eventBuffer = new TSLEventBuffer();
    }

    public String getPlatform() {
        return platform;
    }

    public Path getLogsPath() {
        return logsPath;
    }

    public JSEngine getJsEngine() {
        return jsEngine;
    }

    public TSLPluginManager getPluginManager() {
        return pluginManager;
    }

    public TSLEventBuffer getEventBuffer() {
        return eventBuffer;
    }

    public boolean perform(TSLRuleset ruleset, TSLEvent event, GoodieObject eventArguments) {
        TSLContext context = new TSLContext(this);
        context.setEvent(event);
        context.setEventArguments(eventArguments);
        return ruleset.perform(context);
    }

    /* ------------------------ */

    private void registerCorelib(TSLFunctionLibrary entry) {
        TSLFunctionsCorelib corelib = (TSLFunctionsCorelib) entry;
        FUNC_CORELIB_REGISTRY.register(corelib);
        jsEngine.loadCoreLibrary(corelib);
    }

    @Nullable
    public TSLTag getTag(TSLPlainWord tagNameToken) {
        return getTag(Collections.emptyMap(), tagNameToken);
    }

    @Nullable
    public TSLTag getTag(TSLRuleset ruleset, TSLPlainWord tagNameToken) {
        return getTag(ruleset.getImportedPlugins(), tagNameToken);
    }

    @Nullable
    public TSLTag getTag(Map<String, String> pluginAliases, TSLPlainWord tagNameToken) {
        String namespace = tagNameToken.getNamespace();
        String value = tagNameToken.getValue();

        if (namespace == null) {
            return getDefinition(TAG_REGISTRY, value);
        }

        String pluginId = pluginAliases.get(namespace);

        if (pluginId == null) {
            throw new TSLImportError("Could not resolve " + namespace, tagNameToken);
        }

        return getDefinition(TAG_REGISTRY, pluginId + ":" + value);
    }

    /* ------------------------ */

    @Nullable
    public TSLDecorator getDecorator(TSLDecoratorCall decoratorCallToken) {
        return getDecorator(Collections.emptyMap(), decoratorCallToken);
    }

    @Nullable
    public TSLDecorator getDecorator(TSLRuleset ruleset, TSLDecoratorCall decoratorCallToken) {
        return getDecorator(ruleset.getImportedPlugins(), decoratorCallToken);
    }

    @Nullable
    public TSLDecorator getDecorator(Map<String, String> pluginAliases, TSLDecoratorCall decoratorCallToken) {
        String namespace = decoratorCallToken.getNamespace();
        String value = decoratorCallToken.getName();

        if (namespace == null) {
            return getDefinition(DECORATOR_REGISTRY, value);
        }

        String pluginId = pluginAliases.get(namespace);

        if (pluginId == null) {
            throw new TSLImportError("Could not resolve " + namespace, decoratorCallToken);
        }

        return getDefinition(DECORATOR_REGISTRY, pluginId + ":" + value);
    }

    /* ------------------------ */

    @Nullable
    public TSLEvent getEvent(List<TSLPlainWord> eventTokens) {
        return getEvent(Collections.emptyMap(), eventTokens);
    }

    @Nullable
    public TSLEvent getEvent(TSLRuleset ruleset, List<TSLPlainWord> eventTokens) {
        return getEvent(ruleset.getImportedPlugins(), eventTokens);
    }

    @Nullable
    public TSLEvent getEvent(Map<String, String> pluginAliases, List<TSLPlainWord> eventTokens) {
        String eventExpression = eventTokens.stream()
                .map(TSLPlainWord::getRaw)
                .collect(Collectors.joining(" "));

        Pattern eventPattern = Pattern.compile("(.*) FROM (.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = eventPattern.matcher(eventExpression);

        if (matcher.matches()) {
            String eventName = matcher.group(1);
            String namespace = matcher.group(2);

            String pluginId = pluginAliases.get(namespace);

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

    /* ------------------------ */

    @Nullable
    public TSLAction getAction(TSLPlainWord actionName) {
        return getAction(Collections.emptyMap(), actionName);
    }

    @Nullable
    public TSLAction getAction(TSLRuleset ruleset, TSLPlainWord actionName) {
        return getAction(ruleset.getImportedPlugins(), actionName);
    }

    @Nullable
    public TSLAction getAction(Map<String, String> pluginAliases, TSLPlainWord actionName) {
        String namespace = actionName.getNamespace();
        String value = actionName.getValue();

        if (namespace == null) {
            return getDefinition(ACTION_REGISTRY, value);
        }

        String pluginId = pluginAliases.get(namespace);

        if (pluginId == null) {
            throw new TSLImportError("Could not resolve " + namespace, actionName);
        }

        return getDefinition(ACTION_REGISTRY, pluginId + ":" + value);
    }

    /* ------------------------ */

    @Nullable
    public TSLPredicate getPredicate(String id) {
        return getDefinition(PREDICATE_REGISTRY, id);
    }

    /* ------------------------ */

    @Nullable
    public TSLComparator getComparator(String id) {
        return getDefinition(COMPARATOR_REGISTRY, id);
    }

    /* ------------------------ */

    @Nullable
    public TSLFunctionLibrary getFunctionLibrary(String id) {
        return getDefinition(FUNC_LIBRARY_REGISTRY, id);
    }

    /* ------------------------ */

    @Nullable
    public <T extends TSLDefinition> T getDefinition(TSLRegistry<T> registry, String id) {
//        System.out.println(registry.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
//        System.out.println("Get Definition; " + id);
        if (!id.contains(":")) {
            for (TSLCorePlugin corePlugin : pluginManager.getCorePlugins()) {
                String pluginId = corePlugin.getDescriptor().getPluginId();
                if (registry.has(pluginId + ":" + id)) {
                    return registry.get(pluginId + ":" + id);
                }
            }
            return null;
        }
        return registry.get(id);
    }

    /* ------------------------ */

    public static class Bootstrapper {

        protected final String platform;
        protected Path logsPath = Paths.get("logs");
        protected List<Path> pluginPaths = new LinkedList<>();
        protected List<Class<? extends TSLCorePlugin>> corePluginClasses = new LinkedList<>();

        public Bootstrapper(String platform) {
            this.platform = platform;
        }

        public Bootstrapper logsPath(Path logsPath) {
            this.logsPath = logsPath;
            return this;
        }

        public Bootstrapper pluginPath(Path pluginPath) {
            this.pluginPaths.add(pluginPath);
            return this;
        }

        public Bootstrapper corePlugin(Class<? extends TSLCorePlugin> corePluginClass) {
            this.corePluginClasses.add(corePluginClass);
            return this;
        }

        public Bootstrapper corePlugins(List<Class<? extends TSLCorePlugin>> corePluginClasses) {
            this.corePluginClasses.addAll(corePluginClasses);
            return this;
        }

        public TheSpawnLanguage bootstrap() {
            return new TheSpawnLanguage(platform, logsPath, pluginPaths, corePluginClasses);
        }

    }

}
