package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.binding.TSLFunctionBinding;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public abstract class TSLFunctionLibrary extends TSLDefinition {

    public static String ROOT_LIBRARY_NAME = "default";

    public TSLFunctionLibrary(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public abstract void composeLibrary(ScriptableObject object);

    protected void registerConst(ScriptableObject object, String name, Object constant) {
        object.putConst(name, object, constant);
    }

    protected void registerFunction(ScriptableObject object, TSLFunction function) {
        object.putConst(function.getName(), object, function);
    }

    protected void registerFunction(ScriptableObject object, String name, TSLFunctionBinding binding) {
        object.putConst(name, object, new TSLFunction() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
                return binding.call(context, scope, arguments);
            }
        });
    }

    protected void registerModule(ScriptableObject object, TSLFunctionLibrary library) {
        NativeObject module = new NativeObject();
        library.composeLibrary(module);
        object.put(library.getName(), object, module);
    }

}
