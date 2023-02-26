package net.programmer.igoodie.tsl.registry;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class TSLRegistry<T extends TSLRegistrable> implements Iterable<Map.Entry<String, T>> {

    protected int capacity = -1;
    protected Function<String, String> idMapper;
    protected Map<String, T> registry = new HashMap<>();

    public static <T extends TSLRegistrable> TSLRegistry<T> createWithCapacity(int capacity) {
        TSLRegistry<T> registry = new TSLRegistry<>();
        registry.capacity = capacity;
        return registry;
    }

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

        if (capacity != -1 && registry.size() >= capacity) {
            throw new IllegalArgumentException("");
        }

        registry.put(entry.getRegistryId(), entry);
        postRegister(entry);
        return entry;
    }

    public void unregister(T entry) {
        registry.remove(entry.getRegistryId());
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

    @NotNull
    @Override
    public Iterator<Map.Entry<String, T>> iterator() {
        return registry.entrySet().iterator();
    }

    public Stream<Map.Entry<String, T>> stream() {
        return registry.entrySet().stream();
    }

}
