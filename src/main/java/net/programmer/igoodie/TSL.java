package net.programmer.igoodie;

import net.programmer.igoodie.node.TSLAction;

import java.util.HashMap;
import java.util.Map;

public final class TSL {

    private final String platformName;
    private final Map<String, TSLAction.Supplier<?>> actionDefinitions;

    public TSL(String platformName) {
        this.platformName = platformName;
        this.actionDefinitions = new HashMap<>();
    }

    public <T extends TSLAction.Supplier<?>> T registerAction(String name, T action) {
        this.actionDefinitions.put(name.toUpperCase(), action);
        return action;
    }

}
