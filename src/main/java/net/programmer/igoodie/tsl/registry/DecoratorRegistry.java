package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;

import java.util.HashMap;
import java.util.Map;

public class DecoratorRegistry implements ITSLRegistry<TSLDecorator> {

    // "decorator" -> TSLDecorator
    protected Map<String, TSLDecorator> registry;

    public DecoratorRegistry() {
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLDecorator decorator) {
        if (has(decorator))
            throw new IllegalArgumentException("Cannot register decorator, it is already registered -> " + decorator);

        registry.put(decorator.getName(), decorator);
    }

    @Override
    public boolean has(TSLDecorator decorator) {
        return has(decorator.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(name);
    }

    @Override
    public TSLDecorator get(String name) {
        return registry.get(name);
    }

}
