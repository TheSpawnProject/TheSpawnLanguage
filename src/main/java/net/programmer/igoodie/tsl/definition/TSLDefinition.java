package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Nullable
    public List<Couple<String, String>> getCompletionSnippets() {
        return null;
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
