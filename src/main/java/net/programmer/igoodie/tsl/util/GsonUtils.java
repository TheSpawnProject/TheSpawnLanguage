package net.programmer.igoodie.tsl.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Function;

public class GsonUtils {

    public static JsonObject mergeOverriding(JsonObject a, JsonObject b) {
        JsonObject merged = a.deepCopy();

        for (String field : b.keySet()) {
            merged.add(field, b.get(field));
        }

        return merged;
    }

    public static JsonObject mapKeys(JsonObject object, Function<String, String> mapper) {
        JsonObject copiedObject = object.deepCopy();
        for (String key : object.keySet()) {
            String mappedKey = mapper.apply(key);
            JsonElement removedValue = copiedObject.remove(key);
            copiedObject.add(mappedKey, removedValue);
        }
        return copiedObject;
    }

}
