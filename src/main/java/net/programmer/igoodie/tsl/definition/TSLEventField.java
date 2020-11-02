package net.programmer.igoodie.tsl.definition;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

public abstract class TSLEventField<T> extends TSLDefinition {

    public TSLEventField(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    /* ---------------------------------- */

    public abstract T extractValue(JsonObject json);

}
