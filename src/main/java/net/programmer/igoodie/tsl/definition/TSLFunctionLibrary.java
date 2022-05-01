package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
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

    protected void registerModule(ScriptableObject object, TSLFunctionLibrary library) {
        NativeObject module = new NativeObject();
        library.composeLibrary(module);
        object.put(library.getName(), object, module);
    }

}
