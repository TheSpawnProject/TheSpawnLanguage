package net.programmer.igoodie.tsl.definition;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.util.List;

public abstract class TSLDecorator extends TSLTag {

    public TSLDecorator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public abstract JsonObject evaluateAttributes(TSLString decoratorName, List<TSLString> args);

}
