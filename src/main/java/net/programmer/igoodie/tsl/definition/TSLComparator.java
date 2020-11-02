package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.plugin.TSLPlugin;

public abstract class TSLComparator extends TSLDefinition {

    public TSLComparator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public abstract boolean compare(Object lefthand, String righthand);

    protected boolean parseBoolean(String arg, boolean defaultValue) {
        if (arg.equals("true"))
            return true;
        else if (arg.equals("false"))
            return false;
        else return defaultValue;
    }

    protected double parseDouble(String arg, double defaultValue) {
        try {
            return Double.parseDouble(arg);

        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
