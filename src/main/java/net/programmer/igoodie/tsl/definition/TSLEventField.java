package net.programmer.igoodie.tsl.definition;

import com.google.gson.JsonObject;

public abstract class TSLEventField<T> extends TSLDefinition {

    public TSLEventField(String name) {
        super(name);
    }

    /* ---------------------------------- */

    public abstract T extractValue(JsonObject json);

}
