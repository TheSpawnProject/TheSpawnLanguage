package net.programmer.igoodie.tsl.definition;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;

import java.util.List;

public abstract class TSLTag extends TSLDefinition {

    public TSLTag(String name) {
        super(name);
    }

    public abstract JsonObject compose(TSLString tagName, List<TSLString> args);

    protected boolean parseBoolean(TSLString arg) {
        String argLowercase = arg.getWord().toLowerCase();

        if (argLowercase.equals("true"))
            return true;
        else if (argLowercase.equals("false"))
            return false;
        else throw new TSLSyntaxError("Expected either 'true' or 'false'.", arg);
    }

    protected double parseDouble(TSLString arg) {
        try {
            return Double.parseDouble(arg.getWord());

        } catch (NumberFormatException e) {
            throw new TSLSyntaxError("Expected number.", arg);
        }
    }

}
