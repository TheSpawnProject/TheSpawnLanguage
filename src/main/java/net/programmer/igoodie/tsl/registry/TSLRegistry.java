package net.programmer.igoodie.tsl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TSLRegistry<T extends TSLRegistrable> {

    protected Function<String, String> idMapper;
    protected Map<String, T> registry = new HashMap<>();

    public TSLRegistry() {
        this(i -> i);
    }

    public TSLRegistry(Function<String, String> idMapper) {
        this.idMapper = idMapper;
    }

    public T register(T entry) {
        if (has(entry)) {
            throw new IllegalArgumentException("Cannot register entry, it is already registered -> " + entry.getRegistryId());
        }

        registry.put(entry.getRegistryId(), entry);
        postRegister(entry);
        return entry;
    }

    public void postRegister(T entry) {}

    public boolean has(T entry) {
        return has(entry.getRegistryId());
    }

    public boolean has(String registryId) {
        return registry.containsKey(idMapper.apply(registryId));
    }

    public T get(String registryId) {
        return registry.get(idMapper.apply(registryId));
    }

}
