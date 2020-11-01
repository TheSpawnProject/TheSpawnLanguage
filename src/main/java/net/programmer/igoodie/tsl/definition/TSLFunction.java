package net.programmer.igoodie.tsl.definition;

public abstract class TSLFunction extends TSLDefinition {

    protected boolean global;

    public TSLFunction(String name, boolean global) {
        super(name);
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    public abstract Object getBindingObject();

}
