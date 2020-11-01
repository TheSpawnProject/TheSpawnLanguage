package net.programmer.igoodie.tsl.definition;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.parser.token.TSLString;

import java.util.List;

public abstract class TSLDecorator extends TSLTag {

    public TSLDecorator(String name) {
        super(name);
    }

    @Override
    public abstract JsonObject compose(TSLString tagName, List<TSLString> args);

}
