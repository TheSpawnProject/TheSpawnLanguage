package net.programmer.igoodie.tsl.function.encoder;

import org.mozilla.javascript.NativeObject;

public abstract class JSObjectEncoder<T> {

    protected T subject;

    public JSObjectEncoder(T subject) {
        this.subject = subject;
    }

    public abstract NativeObject encode();

}