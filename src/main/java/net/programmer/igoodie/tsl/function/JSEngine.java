package net.programmer.igoodie.tsl.function;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.registry.FunctionRegistry;

import javax.script.*;
import java.util.Map;

public class JSEngine {

    protected ScriptEngine engine;

    protected FunctionRegistry functionRegistry;

    public JSEngine(FunctionRegistry functionRegistry) {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        this.functionRegistry = functionRegistry;
    }

    public void putGlobalBinding(String name, Object object) {
        this.engine.getContext().setAttribute(name, object, ScriptContext.GLOBAL_SCOPE);
    }

    public String evaluate(String script, TSLContext context) {
        try {
            Object eval = engine.eval(script, createBindings(context));

            if (eval instanceof String)
                return (String) eval;
            else if (eval instanceof Number)
                return String.valueOf(eval);
            else if (eval instanceof Boolean)
                return String.valueOf(eval);
            else if (eval instanceof ScriptObjectMirror)
                return stringify((ScriptObjectMirror) eval);

            return eval.toString();

        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String stringify(ScriptObjectMirror jsonMirror) {
        String delimiter = "";
        StringBuilder builder = new StringBuilder("{ ");

        for (Map.Entry<String, Object> field : jsonMirror.entrySet()) {
            builder.append(delimiter)
                    .append(field.getKey())
                    .append(": ")
                    .append(field.getValue());

            delimiter = ", ";
        }

        builder.append("}");

        return builder.toString();
    }

    private Bindings createBindings(TSLContext context) {
        Bindings bindings = engine.createBindings();

        functionRegistry.forEach((key, value) -> bindings.put(key, value.getBindingObject()));

        if (context != null) {
            GoodieObject eventArguments = context.getEventArguments();
            for (String fieldName : eventArguments.keySet()) {
                bindings.put(fieldName, extractField(eventArguments, fieldName));
            }
        }

        return bindings;
    }

    private Object extractField(GoodieObject eventArguments, String fieldName) {
        GoodieElement argument = eventArguments.get(fieldName);
        try {return argument.asPrimitive().getNumber();} catch (Throwable ignored) {}
        try {return argument.asPrimitive().getBoolean();} catch (Throwable ignored) {}
        return argument.asPrimitive().getString();
    }

}
