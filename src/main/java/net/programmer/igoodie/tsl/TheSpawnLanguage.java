package net.programmer.igoodie.tsl;

import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.registry.ActionRegistry;
import net.programmer.igoodie.tsl.registry.EventFieldRegistry;
import net.programmer.igoodie.tsl.registry.EventRegistry;
import net.programmer.igoodie.tsl.registry.FunctionRegistry;

public class TheSpawnLanguage {

    public final EventRegistry EVENT_REGISTRY;
    public final EventFieldRegistry EVENT_FIELD_REGISTRY;
    public final ActionRegistry ACTION_REGISTRY;
    public final FunctionRegistry FUNCTION_REGISTRY;

    protected final JSEngine jsEngine;

    public TheSpawnLanguage() {
        EVENT_REGISTRY = new EventRegistry();
        EVENT_FIELD_REGISTRY = new EventFieldRegistry();
        ACTION_REGISTRY = new ActionRegistry();
        FUNCTION_REGISTRY = new FunctionRegistry();

        jsEngine = new JSEngine(FUNCTION_REGISTRY);
        jsEngine.putGlobalBinding("TSL_VERSION", "0.0.0");
    }

}
