package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ComparatorRegistry implements ITSLRegistry<TSLComparator> {

    // "IN RANGE" -> TSLComparator
    protected Map<String, TSLComparator> registry;

    public ComparatorRegistry() {
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLComparator comparator) {
        if (has(comparator))
            throw new IllegalArgumentException("Cannot register comparator, it is already registered -> " + comparator);

        registry.put(comparator.getName(), comparator);
    }

    @Override
    public boolean has(TSLComparator comparator) {
        return has(comparator.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(StringUtils.allUpper(name));
    }

    @Override
    public TSLComparator get(String name) {
        return registry.get(StringUtils.allUpper(name));
    }

}
