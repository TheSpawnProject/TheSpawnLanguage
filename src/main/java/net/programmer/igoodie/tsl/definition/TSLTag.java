package net.programmer.igoodie.tsl.definition;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.util.List;

// #! A_TAG WITH_PARAM1 WITH_PARAM2
public abstract class TSLTag extends TSLDefinition {

    public TSLTag(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public abstract JsonObject evaluateAttributes(TSLString tagName, List<TSLString> args);

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
