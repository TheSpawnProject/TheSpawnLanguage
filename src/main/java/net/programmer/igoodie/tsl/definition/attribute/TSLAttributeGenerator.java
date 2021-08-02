package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TSLAttributeGenerator extends TSLDefinition {

    // Default accessor; disallow external usage.
    TSLAttributeGenerator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public final GoodieObject evaluateAttributesWithNamespace(List<TSLToken> tokens) {
        TSLPlugin plugin = getPlugin();
        return GoodieUtils.mapKeys(evaluateAttributes(tokens), plugin::prependNamespace);
    }

    @NotNull
    public abstract GoodieObject evaluateAttributes(List<TSLToken> tokens) throws TSLRuntimeError;

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
