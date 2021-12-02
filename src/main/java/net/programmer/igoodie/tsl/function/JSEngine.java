package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.binding.JSFunctionBinding;
import net.programmer.igoodie.tsl.function.binding.JSLibraryBinding;
import org.mozilla.javascript.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Rhino syntax compatibility: https://mozilla.github.io/rhino/compat/engines.html
public class JSEngine {

    private boolean sealed;
    private final Context jsContext;
    private final ScriptableObject globalScope;

    public JSEngine() {
        this.jsContext = Context.enter();
        this.jsContext.setOptimizationLevel(-1);
        this.jsContext.setMaximumInterpreterStackDepth(255);
        this.jsContext.setLanguageVersion(Context.VERSION_ES6);

        this.globalScope = this.jsContext.initSafeStandardObjects();

        this.jsContext.seal(null);
    }

    public boolean defineConst(String name, Object value) {
        if (sealed) return false;
        if (this.globalScope.has(name, globalScope)) return false;
        this.globalScope.putConst(name, globalScope, value);
        return true;
    }

    public boolean loadLibrary(JSLibraryBinding library) {
        if (sealed) return false;
        if (this.globalScope.has(library.getName(), globalScope)) return false;
        this.globalScope.put(library.getName(), globalScope, library);
        return true;
    }

    public boolean loadFunction(TSLFunction function) {
        JSFunctionBinding binding = function.getBinding();
        this.globalScope.defineProperty(function.getName(), binding,
                ScriptableObject.PERMANENT | ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        return true;
    }

    public void seal() {
        this.sealed = true;
    }

    public ScriptableObject createChildScope() {
        ScriptableObject scope = (ScriptableObject) jsContext.newObject(globalScope);
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
        Scriptable scope = tslContext.getScope();
        return evaluate(script, scope == null ? globalScope : scope);
    }

    public String evaluate(String script) throws EcmaError {
        return evaluate(script, createChildScope());
    }

    public String evaluate(String script, Scriptable scope) throws EcmaError {
        String sourceName = "immediate_evaluator";
        Object evaluation = this.jsContext.evaluateString(scope, script, sourceName, 0, null);
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
