package net.programmer.igoodie.tsl.runtime.token;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLExpression extends TSLToken {

    protected final String expression;

    public TSLExpression(String script) {
        this.expression = script;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        TSLPlatform platform = ctx.getPlatform();
        return platform.getExpressionEvaluator().evaluate(ctx, expression);
    }

    public interface Evaluator {
        String evaluate(TSLEventContext ctx, String expression);
    }

}
