package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.parser.TSLCapture;

public class TSLCaptureCall extends TSLToken {

    protected TSLCapture referredCapture;

    public TSLCaptureCall(int line, int character, TSLCapture referredCapture) {
        super(line, character);
        this.referredCapture = referredCapture;
    }

    public TSLCapture getReferredCapture() {
        return referredCapture;
    }

    @Override
    public String getRaw() {
        return "$" + referredCapture.getName();
    }

    @Override
    public String evaluate(TSLContext context) {
        throw new UnsupportedOperationException();
    }

}
