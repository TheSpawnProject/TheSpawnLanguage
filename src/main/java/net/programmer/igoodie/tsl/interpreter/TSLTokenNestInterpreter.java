package net.programmer.igoodie.tsl.interpreter;

import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLTokenNest;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.AstUtils;
import org.antlr.v4.runtime.Token;

public class TSLTokenNestInterpreter extends TSLInterpreter<TSLTokenNest, TSLParserImpl.WordNestContentContext> {

    protected TSLTokenNest.Builder builder = new TSLTokenNest.Builder();

    @Override
    protected TSLTokenNest yieldValue(TSLParserImpl.WordNestContentContext tree) {
        return builder.build();
    }

    @Override
    public TSLTokenNest visitWord(TSLParserImpl.WordContext ctx) {
        TSLToken token = new TSLTokenInterpreter().interpret(ctx);
        builder.push(token);

        return null;
    }

    @Override
    public TSLTokenNest visitDanglingKeyword(TSLParserImpl.DanglingKeywordContext ctx) {
        Token astToken = AstUtils.getTerminalNodes(ctx).get(0).getSymbol();
        TSLToken token = new TSLPlainWord(astToken.getText()).setSource(astToken);
        builder.push(token);

        return null;
    }

    @Override
    public TSLTokenNest visitWordNest(TSLParserImpl.WordNestContext ctx) {
        TSLTokenNest tokenNest = new TSLTokenNestInterpreter().interpret(ctx.wordNestContent());
        builder.push(tokenNest);

        return null;
    }

}
