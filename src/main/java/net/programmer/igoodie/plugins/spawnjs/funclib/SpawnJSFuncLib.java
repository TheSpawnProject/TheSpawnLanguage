package net.programmer.igoodie.plugins.spawnjs.funclib;

import net.programmer.igoodie.plugins.spawnjs.SpawnJS;
import net.programmer.igoodie.plugins.spawnjs.funclib.os.OsModule;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import org.mozilla.javascript.ScriptableObject;

public class SpawnJSFuncLib extends TSLFunctionLibrary {

    public static final SpawnJSFuncLib INSTANCE = new SpawnJSFuncLib();

    private SpawnJSFuncLib() {
        super(SpawnJS.PLUGIN_INSTANCE, ROOT_LIBRARY_NAME);
    }

    @Override
    public void composeLibrary(ScriptableObject object) {
        registerModule(object, new OsModule("os"));
    }

}
