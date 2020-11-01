package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class EventRegistry implements ITSLRegistry<TSLEvent> {

    // "Event Name" -> TSLEvent
    protected Map<String, TSLEvent> registry;

    public EventRegistry() {
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLEvent event) {
        if (has(event))
            throw new IllegalArgumentException("Cannot register event, it is already registered -> " + event);

        registry.put(event.getName(), event);
    }

    @Override
    public boolean has(TSLEvent event) {
        return has(event.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(StringUtils.upperFirstLetters(name));
    }

    @Override
    public TSLEvent get(String name) {
        return registry.get(StringUtils.upperFirstLetters(name));
    }

}
