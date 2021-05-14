package net.programmer.igoodie.tsl.definition.attribute;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.util.GsonUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TSLAttributeGenerator extends TSLDefinition {

    // Default accessor; disallow external usage.
    TSLAttributeGenerator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public final JsonObject evaluateAttributesWithNamespace(List<TSLToken> tokens) {
        TSLPlugin plugin = getPlugin();
        return GsonUtils.mapKeys(evaluateAttributes(tokens), plugin::prependNamespace);
    }

    @NotNull
    public abstract JsonObject evaluateAttributes(List<TSLToken> tokens) throws TSLRuntimeError;

    /* ------------------------------------ */

    protected boolean parseBoolean(TSLString argument) throws TSLSyntaxError {
        String argLowercase = argument.getWord().toLowerCase();

        if (argLowercase.equals("true"))
            return true;
        else if (argLowercase.equals("false"))
            return false;
        else throw new TSLSyntaxError("Expected either 'true' or 'false'", argument);
    }

    protected double parseDouble(TSLString argument) throws TSLSyntaxError {
        try {
            return Double.parseDouble(argument.getWord());

        } catch (NumberFormatException e) {
            throw new TSLSyntaxError("Expected number", argument);
        }
    }

}
