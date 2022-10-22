package net.programmer.igoodie.tsl.function.scope;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.function.JSContextManager;
import net.programmer.igoodie.tsl.function.JSEngine;
import org.mozilla.javascript.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSScope extends NativeObject {

    protected final JSEngine jsEngine;
    protected final JSScopeMetaVariables metaVariables;

    public JSScope(JSEngine jsEngine) {
        this.jsEngine = jsEngine;
        this.metaVariables = new JSScopeMetaVariables(this);
    }

    public JSEngine getJsEngine() {
        return jsEngine;
    }

    public JSScopeMetaVariables meta() {
        return metaVariables;
    }

    /* ---------------------------- */

    public JSScope fork() {
        JSScope clonedScope = JSContextManager.cloneScope(this);
        clonedScope.defineProperty("__dumpscope", new BaseFunction() {
            @Override
            public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                debugDumpScope(); //TODO: Rework dis
                return Undefined.instance;
            }
        }, ScriptableObject.READONLY);
        return clonedScope;
    }

    public void touchGlobalObject(String objectName, Consumer<NativeObject> touch) {
        NativeObject globalObject;

        if (this.has(objectName, this)) {
            Object globalValue = this.get(objectName, this);
            if (!(globalValue instanceof NativeObject)) {
                String foundTypeName = globalValue.getClass().getSimpleName();
                throw new TSLRuntimeError("Cannot touch '" + objectName +
                        "'. Because it already exists and its type is not NativeObject. It is " + foundTypeName);
            }
            globalObject = ((NativeObject) globalValue);

        } else {
            globalObject = new NativeObject();
        }

        touch.accept(globalObject);
        this.put(objectName, this, globalObject);
    }

    /* ---------------------------- */

    public String stringify(Object value) {
        if (value instanceof NativeJavaObject) return ((NativeJavaObject) value).unwrap().toString();
        if (value instanceof NativeObject) return stringifyObject((NativeObject) value);
        if (value instanceof NativeArray) return stringifyArray((NativeArray) value);
        if (value instanceof BaseFunction) return "[Func:" + ((BaseFunction) value).getFunctionName() + "]";
        if (value instanceof Undefined) return "undefined";
        if (value == null) return "null";

        return value.toString();
    }

    private String stringifyObject(NativeObject object) {
        String delimiter = "";
        StringBuilder builder = new StringBuilder("{ ");

        for (Map.Entry<Object, Object> field : object.entrySet()) {
            Object key = field.getKey();
            Object value = field.getValue();

            builder.append(delimiter).append(key).append(": ").append(stringify(value));

            delimiter = ", ";
        }

        builder.append(" }");

        return builder.toString();
    }

    private String stringifyArray(NativeArray array) {
        return (((Stream<?>) array.stream())).map(this::stringify).collect(Collectors.joining(", ", "[", "]"));
    }

    /* ---------------------------- */

    public String debugDumpScope() {
        StringBuilder dump = new StringBuilder("============================").append("\n");
        String target = this.metaVariables.scriptFilename.get().orElseGet(() -> String.valueOf(this.hashCode()));

        dump.append("Dumping scope in ").append(target).append("\n");

        Arrays.stream(this.getIds()).sorted().forEach(id -> {
            dump.append(id).append(" = ").append(stringify(this.get(id)).replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r"));
        });

        dump.append("============================");

        return dump.toString();
    }

}
