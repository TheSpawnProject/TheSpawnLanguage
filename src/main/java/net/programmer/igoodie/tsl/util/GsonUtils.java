package net.programmer.igoodie.tsl.util;

import com.google.gson.JsonObject;

public class GsonUtils {

    public static JsonObject mergeOverriding(JsonObject a, JsonObject b) {
        JsonObject merged = a.deepCopy();

        for (String field : b.keySet()) {
            merged.add(field, b.get(field));
        }

        return merged;
    }

}
