package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

public abstract class TSLDefinition {

    protected TSLPlugin plugin;
    protected String name;

    public TSLDefinition(TSLPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public TSLPlugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getName());
    }

    /* ----------------------------- */

    protected boolean parseBoolean(String arg, boolean defaultValue) {
        if (arg.equals("true"))
            return true;
        else if (arg.equals("false"))
            return false;
        else return defaultValue;
    }

    protected boolean parseBoolean(TSLString argument) throws TSLSyntaxError {
        String argLowercase = argument.getWord().toLowerCase();

        if (argLowercase.equals("true"))
            return true;
        else if (argLowercase.equals("false"))
            return false;
        else throw new TSLSyntaxError("Expected either 'true' or 'false'", argument);
    }

    protected double parseDouble(String arg, double defaultValue) {
        try {
            return Double.parseDouble(arg);

        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    protected double parseDouble(TSLString argument) throws TSLSyntaxError {
        try {
            return Double.parseDouble(argument.getWord());

        } catch (NumberFormatException e) {
            throw new TSLSyntaxError("Expected number", argument);
        }
    }

}
