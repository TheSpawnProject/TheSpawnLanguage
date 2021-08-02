package net.programmer.igoodie.tsl.util;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

import java.util.function.Function;

public class GoodieUtils {

    public static GoodieObject mergeOverriding(GoodieObject a, GoodieObject b) {
        GoodieObject merged = (GoodieObject) a.deepCopy();

        for (String field : b.keySet()) {
            merged.put(field, b.get(field));
        }

        return merged;
    }

    public static GoodieObject mapKeys(GoodieObject object, Function<String, String> mapper) {
        GoodieObject copiedObject = (GoodieObject) object.deepCopy();

        for (String key : object.keySet()) {
            String mappedKey = mapper.apply(key);
            GoodieElement removedValue = copiedObject.remove(key);
            copiedObject.put(mappedKey, removedValue);
        }

        return copiedObject;
    }

}
