package net.programmer.igoodie.tsl.function.format;

import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.exception.YetToBeImplementedException;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

public class NativeJsObjectGoodieFormat extends GoodieFormat<NativeObject, GoodieObject> {

    @Override
    public GoodieObject writeToGoodie(NativeObject nativeObject) {
        throw new YetToBeImplementedException();
    }

    /* ---------------------- */

    @Override
    public NativeObject readFromGoodie(GoodieObject goodieObject) {
        return convertObject(goodieObject);
    }

    public static Object convert(GoodieElement goodieElement) {
        if (goodieElement.isObject())
            return convertObject(goodieElement.asObject());
        if (goodieElement.isArray())
            return convertArray(goodieElement.asArray());
        if (goodieElement.isPrimitive())
            return goodieElement.asPrimitive().get();
        if (goodieElement.isNull())
            return null;

        return null;
    }

    public static NativeObject convertObject(GoodieObject goodieObject) {
        NativeObject jsObject = new NativeObject();
        for (String propertyName : goodieObject.keySet()) {
            Object value = convert(goodieObject.get(propertyName));
            jsObject.defineProperty(propertyName, value, ScriptableObject.CONST);
        }
        return jsObject;
    }

    public static NativeArray convertArray(GoodieArray goodieArray) {
        NativeArray jsArray = new NativeArray(goodieArray.size());
        for (int i = 0; i < goodieArray.size(); i++) {
            GoodieElement goodieElement = goodieArray.get(i);
            Object value = convert(goodieElement);
            jsArray.put(i, jsArray, value);
        }
        return jsArray;
    }

    /* ---------------------- */

    @Override
    public String writeToString(NativeObject nativeObject, boolean pretty) {
        throw new YetToBeImplementedException();
    }

    @Override
    public NativeObject readFromString(String string) throws GoodieParseException {
        throw new YetToBeImplementedException();
    }

}
