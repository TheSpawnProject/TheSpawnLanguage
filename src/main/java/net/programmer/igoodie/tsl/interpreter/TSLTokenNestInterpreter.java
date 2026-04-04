package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLTokenNest;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

public class TSLTokenNestInterpreter extends TSLInterpreter<TSLTokenNest, TSLParserImpl.WordNestContext> {

    protected TSLTokenNest.Builder builder = new TSLTokenNest.Builder();

    @Override
    protected TSLTokenNest yieldValue(TSLParserImpl.WordNestContext tree) {
        return builder.build();
    }

    @Override
    public TSLTokenNest visitWord(TSLParserImpl.WordContext ctx) {
        TSLToken token = new TSLTokenInterpreter().interpret(ctx);
        builder.push(token);

        return null;
    }

    @Override
    public TSLTokenNest visitWordNest(TSLParserImpl.WordNestContext ctx) {
        TSLTokenNest tokenNest = new TSLTokenNestInterpreter().interpret(ctx);
        builder.push(tokenNest);

        return null;
    }

}
