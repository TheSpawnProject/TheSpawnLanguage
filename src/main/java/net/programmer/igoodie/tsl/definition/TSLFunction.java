package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.plugin.TSLPlugin;

public abstract class TSLFunction extends TSLDefinition {

    protected boolean global;

    public TSLFunction(TSLPlugin plugin, String name, boolean global) {
        super(plugin, name);
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    public abstract Object getBindingObject();

}
