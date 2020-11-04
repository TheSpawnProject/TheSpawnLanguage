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

    @FunctionalInterface
    public interface withNoParams {
        Object calculate();
    }

    @FunctionalInterface
    public interface with1Param<T1> {
        Object calculate(T1 arg1);
    }

    @FunctionalInterface
    public interface with2Params<T1, T2> {
        Object calculate(T1 arg1, T2 arg2);
    }

    @FunctionalInterface
    public interface with3Params<T1, T2, T3> {
        Object calculate(T1 arg1, T2 arg2, T3 arg3);
    }

    @FunctionalInterface
    public interface with4Params<T1, T2, T3, T4> {
        Object calculate(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
    }


}
