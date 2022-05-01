package net.programmer.igoodie.tsl.definition.base;

import net.programmer.igoodie.tsl.compat.LSPFeatures;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;

public abstract class TSLDefinition implements TSLRegistrable, LSPFeatures {

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
    public String getRegistryId() {
        return plugin.getManifest().getPluginId() + ":" + getName();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getName());
    }

    /* ----------------------------- */

    protected boolean parseBoolean(String arg, boolean defaultValue) {
        if (arg.equalsIgnoreCase("true"))
            return true;
        else if (arg.equalsIgnoreCase("false"))
            return false;
        else return defaultValue;
    }

    protected boolean parseBoolean(TSLPlainWord argument) throws TSLSyntaxError {
        String argLowercase = argument.getWord().toLowerCase();

        if (argLowercase.equals("true"))
            return true;
        else if (argLowercase.equals("false"))
            return false;
        else throw new TSLSyntaxError("Expected either 'true' or 'false'", argument);
    }

    protected int parseInteger(TSLToken token, TSLContext context) {
        try {
            return Integer.parseInt(token.evaluate(context));

        } catch (NumberFormatException e) {
            throw new TSLRuntimeError("Expected integer", token);
        }
    }

    protected double parseDouble(String evaluatedValue) {
        try {
            return Double.parseDouble(evaluatedValue);

        } catch (NumberFormatException e) {
            throw new TSLRuntimeError("Expected floating point number");
        }
    }

    protected double parseDouble(TSLToken token, String evaluatedValue) {
        try {
            return Double.parseDouble(evaluatedValue);

        } catch (NumberFormatException e) {
            throw new TSLRuntimeError("Expected floating point number", token);
        }
    }

    protected double parseDouble(TSLToken token, TSLContext context) {
        return parseDouble(token, token.evaluate(context));
    }

    protected double parseDouble(TSLPlainWord token) {
        return parseDouble(token, token.getWord());
    }

}
