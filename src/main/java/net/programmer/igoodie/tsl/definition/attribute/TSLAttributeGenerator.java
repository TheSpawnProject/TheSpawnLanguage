package net.programmer.igoodie.tsl.definition.attribute;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.util.List;

public abstract class TSLAttributeGenerator extends TSLDefinition {

    // Default accessor; disallow external usage.
    TSLAttributeGenerator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public JsonObject evaluateAttributesWithNamespace(TSLString tagToken, List<TSLString> args) {
        JsonObject withoutNS = evaluateAttributes(tagToken, args);
        JsonObject withNS = new JsonObject();

        TSLPlugin plugin = getPlugin();

        for (String attributeName : withoutNS.keySet()) {
            withNS.add(
                    plugin.prependNamespace(attributeName),
                    withoutNS.get(attributeName)
            );
        }

        return withNS;
    }

    public abstract JsonObject evaluateAttributes(TSLString tagToken, List<TSLString> args);

    protected boolean parseBoolean(TSLString arg) {
        String argLowercase = arg.getWord().toLowerCase();

        if (argLowercase.equals("true"))
            return true;
        else if (argLowercase.equals("false"))
            return false;
        else throw new TSLSyntaxError("Expected either 'true' or 'false'", arg);
    }

    protected double parseDouble(TSLString arg) {
        try {
            return Double.parseDouble(arg.getWord());

        } catch (NumberFormatException e) {
            throw new TSLSyntaxError("Expected number", arg);
        }
    }

}
