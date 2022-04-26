package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import org.mozilla.javascript.ScriptableObject;

public class RootLibrary extends TSLFunctionLibrary {

    public static final RootLibrary INSTANCE = new RootLibrary();

    private RootLibrary() {
        super(ExamplePlugin.PLUGIN_INSTANCE, ROOT_LIBRARY_NAME);
    }

    @Override
    public void composeLibrary(ScriptableObject object) {
        registerFunction(object, CurrentUnixFunction.INSTANCE);
    }

}
