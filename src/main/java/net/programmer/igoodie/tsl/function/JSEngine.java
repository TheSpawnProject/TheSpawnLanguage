package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.binding.JSFunctionBinding;
import net.programmer.igoodie.tsl.function.binding.JSLibraryBinding;
import org.mozilla.javascript.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Rhino syntax compatibility: https://mozilla.github.io/rhino/compat/engines.html
public class JSEngine {

    private boolean sealed;

    private ScriptableObject globalScope;

    private final Map<String, Object> definedConsts = new HashMap<>();
    private final List<JSLibraryBinding> loadedLibraries = new LinkedList<>();
    private final List<TSLFunction> loadedFunctions = new LinkedList<>();

    public JSEngine() {
        ensureContext();
    }

    public Context getJsContext() {
        ensureContext();
        return Context.getCurrentContext();
    }

    private void ensureContext() {
        if (Context.getCurrentContext() == null) {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            context.setMaximumInterpreterStackDepth(255);
            context.setLanguageVersion(Context.VERSION_ES6);

            if (this.globalScope == null) {
                this.globalScope = context.initSafeStandardObjects();
            }

            context.seal(null);

//            definedConsts.forEach((name, value) -> globalScope.putConst(name, globalScope, value));
//            loadedLibraries.forEach(library -> globalScope.put(library.getName(), globalScope, library));
//            loadedFunctions.forEach(function -> globalScope.defineProperty(function.getName(), function.getBinding(),
//                    ScriptableObject.PERMANENT | ScriptableObject.DONTENUM | ScriptableObject.READONLY));
        }
    }

    /* ------------------------ */

    public boolean defineConst(String name, Object value) {
        if (sealed) return false;
        if (this.globalScope.has(name, globalScope)) return false;
        this.globalScope.putConst(name, globalScope, value);
        definedConsts.put(name, value);
        return true;
    }

    public boolean loadLibrary(JSLibraryBinding library) {
        if (sealed) return false;
        if (this.globalScope.has(library.getName(), globalScope)) return false;
        this.globalScope.put(library.getName(), globalScope, library);
        loadedLibraries.add(library);
        return true;
    }

    public boolean loadFunction(TSLFunction function) {
        JSFunctionBinding binding = function.getBinding();
        this.globalScope.defineProperty(function.getName(), binding,
                ScriptableObject.PERMANENT | ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        loadedFunctions.add(function);
        return true;
    }

    public void seal() {
        this.sealed = true;
    }

    public ScriptableObject createChildScope() {
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
                    Object argument = extractField(eventArguments, argumentName);
                    scope.putConst(argumentName, globalScope, argument);
                }
            }
        }
    }

    private Object extractField(GoodieObject eventArguments, String fieldName) {
        if (eventArguments.hasNumber(fieldName))
            return eventArguments.getNumber(fieldName).orElse(-1);
        if (eventArguments.hasBoolean(fieldName))
            return eventArguments.getBoolean(fieldName).orElse(false);
        return eventArguments.getString(fieldName);
    }

    /* ------------------------------------ */

    public String evaluate(String script, TSLContext tslContext) throws EcmaError {
        Scriptable scope = tslContext.getRuleScope();
        return evaluate(script, scope == null ? globalScope : scope);
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
