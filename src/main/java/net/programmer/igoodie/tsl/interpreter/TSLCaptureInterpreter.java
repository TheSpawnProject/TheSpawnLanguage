package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;

public class TSLCaptureInterpreter extends TSLInterpreter<TSLCapture, TSLParserImpl.CaptureRuleContext> {



    @Override
    public TSLCapture yieldValue(TSLParserImpl.CaptureRuleContext tree) {
        return null;
    }

}
