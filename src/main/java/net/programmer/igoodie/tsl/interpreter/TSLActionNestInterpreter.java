package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLActionNest;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;

public class TSLActionNestInterpreter extends TSLInterpreter<TSLActionNest, TSLParserImpl.WordNestContentContext> {

    protected TSLActionNest nest;

    @Override
    protected TSLActionNest yieldValue(TSLParserImpl.WordNestContentContext tree) {
        return nest;
    }

    @Override
    public TSLActionNest visitAction(TSLParserImpl.ActionContext ctx) {
        TSLAction.Deferred deferred = new TSLActionInterpreter().interpret(ctx);
        this.nest = new TSLActionNest(deferred);

        return null;
    }

}
