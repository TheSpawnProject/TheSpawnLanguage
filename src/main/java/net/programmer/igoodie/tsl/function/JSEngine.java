package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.binding.JSFunctionBinding;
import net.programmer.igoodie.tsl.function.binding.JSLibraryBinding;
import org.mozilla.javascript.*;
import org.mozilla.javascript.regexp.NativeRegExp;

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

    private ScriptableObject createChildScope() {
        return new ScriptableObject(globalScope, null) {
            @Override
            public String getClassName() {
                return null;
            }
        };
    }

    /* ------------------------------------ */

    private void loadTSLContext(TSLContext tslContext) {
        if (tslContext != null) {
            GoodieObject eventArguments = tslContext.getEventArguments();
            if (eventArguments != null) {
                for (String argumentName : eventArguments.keySet()) {
                    Object argument = extractField(eventArguments, argumentName);
                    this.globalScope.putConst(argumentName, globalScope, argument);
                }
            }
        }
    }

    private void unloadTSLContext(TSLContext tslContext) {
        if (tslContext != null) {
            GoodieObject eventArguments = tslContext.getEventArguments();
            if (eventArguments != null) {
                for (String argumentName : eventArguments.keySet()) {
                    this.globalScope.delete(argumentName);
                }
            }
        }
    }

    private Object extractField(GoodieObject eventArguments, String fieldName) {
        GoodieElement argument = eventArguments.get(fieldName);
        try {return argument.asPrimitive().getNumber();} catch (Throwable ignored) {}
        try {return argument.asPrimitive().getBoolean();} catch (Throwable ignored) {}
        return argument.asPrimitive().getString();
    }

    /* ------------------------------------ */

    public String evaluate(String script, TSLContext tslContext) {
        loadTSLContext(tslContext);
        String evaluation = evaluate(script);
        unloadTSLContext(tslContext);
        return evaluation;
    }

    public String evaluate(String script) {
        try {
            String sourceName = "immediate_evaluator";
            ScriptableObject childScope = createChildScope();
            Object evaluation = this.jsContext.evaluateString(childScope, script, sourceName, 0, null);
            return stringify(evaluation);

        } catch (Exception e) {
            e.printStackTrace();
            return "#!ERROR!#"; // TODO: Throw appropriate error
        }
    }

    private String stringify(Object value) {
        if (value instanceof NativeJavaObject)
            return ((NativeJavaObject) value).unwrap().toString();
        if (value instanceof NativeObject)
            return stringifyObject((NativeObject) value);
        if (value instanceof NativeArray)
            return stringifyArray((NativeArray) value);
        if (value instanceof NativeRegExp)
            return ""; // TODO: What now?
        if (value instanceof Function)
            return ""; // TODO: What now?
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
