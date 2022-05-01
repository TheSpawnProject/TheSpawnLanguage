package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.exception.TSLImportError;
import net.programmer.igoodie.tsl.function.binding.TSLContextGetter;
import net.programmer.igoodie.tsl.util.AccessUtils;
import org.mozilla.javascript.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Rhino syntax compatibility: https://mozilla.github.io/rhino/compat/engines.html
public class JSEngine {

    private static final Map<Context, ScriptableObject> GLOBALS = new WeakHashMap<>();

    private final Map<String, Object> definedConsts = new HashMap<>();

    public JSEngine() {}

    public Context getJsContext() {
        return Optional.ofNullable(Context.getCurrentContext()).orElseGet(() -> {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            context.setMaximumInterpreterStackDepth(255);
            context.setLanguageVersion(Context.VERSION_ES6);
            return context;
        });
    }

    public ScriptableObject getGlobalScope() {
        Context context = getJsContext();
        return GLOBALS.computeIfAbsent(context, ctx -> ctx.initSafeStandardObjects(null, true));
    }

    /* ------------------------ */

    public void defineConst(String name, Object value) {
        ScriptableObject globalScope = getGlobalScope();
        if (globalScope.has(name, globalScope)) return;
        globalScope.putConst(name, globalScope, value);
        definedConsts.put(name, value);
    }

    // TODO: Shall I yeet this? :thinking:
    public void loadLibrary(ScriptableObject scope, String namespace, TSLFunctionLibrary library) {
        NativeObject libraryObject = scope.has(namespace, scope)
                ? ((NativeObject) scope.get(namespace))
                : new NativeObject();
        library.composeLibrary(libraryObject);
        scope.put(namespace, scope, libraryObject);
    }

    public void loadCoreLibrary(TSLFunctionLibrary coreLibrary) {
        Class<?> accessedFromClass = AccessUtils.accessedFromClass(JSEngine.class);

        if (accessedFromClass != TheSpawnLanguage.class) {
            throw new TSLImportError("Core JS libraries cannot be loaded externally");
        }

        ScriptableObject globalScope = getGlobalScope();
        NativeObject libraryObject = new NativeObject();
        coreLibrary.composeLibrary(libraryObject);

        for (Object key : libraryObject.keySet()) {
            Object value = libraryObject.get(key);
            globalScope.put(key.toString(), globalScope, value);
        }
    }

    public ScriptableObject createChildScope() {
        ScriptableObject globalScope = getGlobalScope();
        ScriptableObject scope = (ScriptableObject) getJsContext().newObject(globalScope);
        scope.setPrototype(globalScope);
        scope.setParentScope(null);
        return scope;
    }

    /* ------------------------------------ */

    public void loadTSLContext(ScriptableObject scope, TSLContext tslContext) {
        if (tslContext != null) {
            scope.putConst("__context", scope, new TSLContextGetter(tslContext));

            GoodieObject eventArguments = tslContext.getEventArguments();

            for (String argumentName : eventArguments.keySet()) {
                Object argument = TSLEvent.extractField(eventArguments, argumentName);
                scope.putConst(argumentName, scope, argument);
            }

            loadPluginLibraries(scope, tslContext.getTsl(), tslContext.getImportedPlugins());
        }
    }

    public void loadPluginLibraries(ScriptableObject scope, TheSpawnLanguage tsl, Map<String, String> importedPlugins) {
        for (Map.Entry<String, String> importEntry : importedPlugins.entrySet()) {
            String alias = importEntry.getKey();
            String pluginId = importEntry.getValue();

            List<TSLFunctionLibrary> associatedModules = tsl.FUNC_LIBRARY_REGISTRY.stream()
                    .map(Map.Entry::getValue)
                    .filter(lib -> lib.getPlugin().getManifest().getPluginId().equals(pluginId))
                    .collect(Collectors.toList());

            if (associatedModules.size() == 0) {
                continue;
            }

            NativeObject importedObject = new NativeObject();

            for (TSLFunctionLibrary module : associatedModules) {
                if (module.getName().equals(TSLFunctionLibrary.ROOT_LIBRARY_NAME)) {
                    module.composeLibrary(importedObject);

                } else {
                    NativeObject moduleObject = new NativeObject();
                    module.composeLibrary(moduleObject);
                    importedObject.put(module.getName(), scope, moduleObject);
                }
            }

            scope.put(alias, scope, importedObject);
        }
    }

    /* ------------------------------------ */

    public String evaluate(String script, TSLContext tslContext) throws EcmaError {
        ScriptableObject scope = tslContext.getRuleScope() == null
                ? createChildScope()
                : tslContext.getRuleScope();

        if (!scope.has("__context", scope)) {
            loadTSLContext(scope, tslContext);
        }

        return evaluate(script, scope);
    }

    public String evaluate(String script) throws EcmaError {
        return evaluate(script, createChildScope());
    }

    public String evaluate(String script, Scriptable scope) throws EcmaError {
        String sourceName = "immediate_evaluator";
        Context context = getJsContext();
        Object evaluation = context.evaluateString(scope, script, sourceName, 0, null);
        return stringify(evaluation);
    }

    /* ------------------------------------ */

    private String stringify(Object value) {
        if (value instanceof NativeJavaObject)
            return ((NativeJavaObject) value).unwrap().toString();
        if (value instanceof NativeObject)
            return stringifyObject((NativeObject) value);
        if (value instanceof NativeArray)
            return stringifyArray((NativeArray) value);
        if (value instanceof BaseFunction)
            return "[Func:" + ((BaseFunction) value).getFunctionName() + "]";
        if (value instanceof Undefined)
            return "undefined";
        if (value == null)
            return "null";

        return value.toString();
    }

    private String stringifyObject(NativeObject object) {
        String delimiter = "";
        StringBuilder builder = new StringBuilder("{ ");

        for (Map.Entry<Object, Object> field : object.entrySet()) {
            Object key = field.getKey();
            Object value = field.getValue();

            builder.append(delimiter)
                    .append(key)
                    .append(": ")
                    .append(stringify(value));

            delimiter = ", ";
        }

        builder.append(" }");

        return builder.toString();
    }

    private String stringifyArray(NativeArray array) {
        return (((Stream<?>) array.stream()))
                .map(this::stringify)
                .collect(Collectors.joining(", ", "[", "]"));
    }

}
