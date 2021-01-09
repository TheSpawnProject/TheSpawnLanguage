package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.parser.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

public class TSLCaptureCall extends TSLToken {

    protected String captureName;

    public TSLCaptureCall(int line, int character, String captureName) {
        super(line, character);
        this.captureName = captureName;
    }

    public String getCaptureName() {
        return captureName;
    }

    public TSLCapture getReferredCapture(TSLRuleset ruleset) {
        return ruleset.getCaptures().get(captureName);
    }

    @Override
    public String getRaw() {
        return "$" + captureName;
    }

    @Override
    public String evaluate(TSLContext context) {
        throw new UnsupportedOperationException();
    }

}
