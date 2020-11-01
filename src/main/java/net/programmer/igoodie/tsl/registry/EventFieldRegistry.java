package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.TSLEventField;

import java.util.HashMap;
import java.util.Map;

public class EventFieldRegistry implements ITSLRegistry<TSLEventField<?>> {

    protected Map<String, TSLEventField<?>> registry;

    public EventFieldRegistry() {
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLEventField<?> field) {
        if (has(field))
            throw new IllegalArgumentException("Cannot register event field, it is already registered -> " + field);

        registry.put(field.getName(), field);
    }

    @Override
    public boolean has(TSLEventField<?> field) {
        return has(field.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(name);
    }

    @Override
    public TSLEventField<?> get(String name) {
        return registry.get(name);
    }

}
