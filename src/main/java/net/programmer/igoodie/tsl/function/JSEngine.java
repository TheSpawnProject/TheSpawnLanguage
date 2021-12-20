package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.binding.JSFunctionBinding;
import net.programmer.igoodie.tsl.function.binding.JSLibraryBinding;
import org.mozilla.javascript.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Rhino syntax compatibility: https://mozilla.github.io/rhino/compat/engines.html
public class JSEngine {

    private static final Map<Context, ScriptableObject> GLOBALS = new WeakHashMap<>();

    private boolean sealed;

    private final Map<String, Object> definedConsts = new HashMap<>();
    private final List<JSLibraryBinding> loadedLibraries = new LinkedList<>();
    private final List<TSLFunction> loadedFunctions = new LinkedList<>();

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

    public boolean defineConst(String name, Object value) {
        if (sealed) return false;
        ScriptableObject globalScope = getGlobalScope();
        if (globalScope.has(name, globalScope)) return false;
        globalScope.putConst(name, globalScope, value);
        definedConsts.put(name, value);
        return true;
    }

    public boolean loadLibrary(JSLibraryBinding library) {
        if (sealed) return false;
        ScriptableObject globalScope = getGlobalScope();
        if (globalScope.has(library.getName(), globalScope)) return false;
        globalScope.put(library.getName(), globalScope, library);
        loadedLibraries.add(library);
        return true;
    }

    public boolean loadFunction(TSLFunction function) {
        ScriptableObject globalScope = getGlobalScope();
        JSFunctionBinding binding = function.getBinding();
        int attributes = ScriptableObject.PERMANENT
                | ScriptableObject.DONTENUM
                | ScriptableObject.READONLY;
        globalScope.defineProperty(function.getName(), binding, attributes);
        loadedFunctions.add(function);
        return true;
    }

    public void seal() {
        this.sealed = true;
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
            GoodieObject eventArguments = tslContext.getEventArguments();
            if (eventArguments != null) {
                for (String argumentName : eventArguments.keySet()) {
                    Object argument = TSLEvent.extractField(eventArguments, argumentName);
                    scope.putConst(argumentName, getGlobalScope(), argument);
                }
            }
        }
    }

    /* ------------------------------------ */

    public String evaluate(String script, TSLContext tslContext) throws EcmaError {
        Scriptable scope = tslContext.getRuleScope();
        return evaluate(script, scope == null ? getGlobalScope() : scope);
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

    private String stringify(Object value) {
        if (value instanceof NativeJavaObject)
            return ((NativeJavaObject) value).unwrap().toString();
        if (value instanceof NativeObject)
            return stringifyObject((NativeObject) value);
        if (value instanceof NativeArray)
            return stringifyArray((NativeArray) value);
        if (value instanceof ArrowFunction)
            return "[ArrowFunc]";
        if (value instanceof NativeFunction)
            return "[NativeFunc]";
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
