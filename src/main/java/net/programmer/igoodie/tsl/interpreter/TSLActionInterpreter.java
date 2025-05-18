package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;

public class TSLActionInterpreter extends TSLInterpreter<TSLAction, TSLParserImpl.ActionArgsContext> {

    @Override
    public TSLAction yieldValue(TSLParserImpl.ActionArgsContext tree) {
        return null;
    }

}
