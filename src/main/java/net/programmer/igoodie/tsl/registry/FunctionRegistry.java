package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.TSLFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FunctionRegistry implements ITSLRegistry<TSLFunction> {

    // "functionName" -> TSLFunction
    protected Map<String, TSLFunction> registry;

    public FunctionRegistry() {
        this.registry = new HashMap<>();
    }

    public void forEach(BiConsumer<String, TSLFunction> functionConsumer) {
        registry.forEach(functionConsumer);
    }

    @Override
    public void register(TSLFunction function) {
        if (has(function))
            throw new IllegalArgumentException("Cannot register function, it is already registered -> " + function);

        registry.put(function.getName(), function);
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
