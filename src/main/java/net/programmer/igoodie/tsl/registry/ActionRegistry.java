package net.programmer.igoodie.tsl.registry;

import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ActionRegistry implements ITSLRegistry<TSLAction> {

    // "ACTION_NAME" -> TSLAction
    protected Map<String, TSLAction> registry;

    public ActionRegistry() {
        this.registry = new HashMap<>();
    }

    @Override
    public void register(TSLAction action) {
        if (has(action))
            throw new IllegalArgumentException("Cannot register action, it is already registered -> " + action);

        registry.put(action.getName(), action);
    }

    @Override
    public boolean has(TSLAction action) {
        return has(action.getName());
    }

    @Override
    public boolean has(String name) {
        return registry.containsKey(StringUtils.allUpper(name));
    }

    @Override
    public TSLAction get(String name) {
        return registry.get(StringUtils.allUpper(name));
    }

}
