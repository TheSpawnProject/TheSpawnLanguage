package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FunctionRegistry implements ITSLRegistry<TSLFunction> {

    protected TheSpawnLanguage language;

    // "_functionName" -> TSLFunction
    protected Map<String, TSLFunction> registry;

    public FunctionRegistry(TheSpawnLanguage language) {
        this.language = language;
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLFunction function) {
        if (has(function))
            throw new IllegalArgumentException("Cannot register function, it is already registered -> " + function);

        registry.put(function.getName(), function);
        language.getJsEngine().loadFunction(function);
    }

    @Override
    public boolean has(TSLFunction function) {
        return has(function.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(name);
    }

    @Override
    public TSLFunction get(String name) {
        return registry.get(name);
    }

}
