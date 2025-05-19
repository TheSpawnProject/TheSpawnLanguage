package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;

public class TSLEventInterpreter extends TSLInterpreter<TSLEvent, TSLParserImpl.EventContext> {

    @Override
    public TSLEvent yieldValue(TSLParserImpl.EventContext tree) {
        return null;
    }

    @Override
    public TSLEvent visitEventName(TSLParserImpl.EventNameContext ctx) {
        return super.visitEventName(ctx);
    }



}
