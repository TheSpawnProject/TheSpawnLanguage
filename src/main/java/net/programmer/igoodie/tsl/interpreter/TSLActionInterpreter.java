package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.defer.TSLActionRef;

public class TSLActionInterpreter extends TSLInterpreter<TSLActionRef, TSLParserImpl.ActionArgsContext> {

    @Override
    public TSLActionRef yieldValue(TSLParserImpl.ActionArgsContext tree) {
        return null;
    }

}
