package net.programmer.igoodie.tsl.function.encoder;

import org.mozilla.javascript.NativeObject;

import java.util.Map;

public class MapEncoder extends JSObjectEncoder<Map<?, ?>> {

    public MapEncoder(Map<?, ?> subject) {
        super(subject);
    }

    @Override
    public NativeObject encode() {
        NativeObject nativeObject = new NativeObject();
        subject.forEach((key, value) -> nativeObject.put(key.toString(), nativeObject, value));
        return nativeObject;
    }

}
