package net.programmer.igoodie.plugins.spawnjs.corelib;

import net.programmer.igoodie.plugins.spawnjs.SpawnJS;
import net.programmer.igoodie.plugins.spawnjs.corelib.functions.PrintFunction;
import net.programmer.igoodie.plugins.spawnjs.corelib.functions.RequireFunction;
import net.programmer.igoodie.tsl.function.TSLFunctionsCorelib;
import org.mozilla.javascript.ScriptableObject;

public class SpawnJSCorelib extends TSLFunctionsCorelib {

    public static final SpawnJSCorelib INSTANCE = new SpawnJSCorelib();

    private SpawnJSCorelib() {
        super(SpawnJS.PLUGIN_INSTANCE);
    }

    @Override
    public void composeLibrary(ScriptableObject object) {
        registerFunction(object, PrintFunction.INSTANCE);
        registerFunction(object, RequireFunction.INSTANCE);
    }

}
