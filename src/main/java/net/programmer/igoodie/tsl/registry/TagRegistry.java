package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class TagRegistry implements ITSLRegistry<TSLTag> {

    // "TSL_TAG" -> TSLTag
    protected Map<String, TSLTag> registry;

    public TagRegistry() {
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLTag tag) {
        if (has(tag))
            throw new IllegalArgumentException("Cannot register tag, it is already registered -> " + tag);

        registry.put(tag.getName(), tag);
    }

    @Override
    public boolean has(TSLTag tag) {
        return has(tag.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(StringUtils.upperSnake(name));
    }

    @Override
    public TSLTag get(String name) {
        return registry.get(name);
    }

}
