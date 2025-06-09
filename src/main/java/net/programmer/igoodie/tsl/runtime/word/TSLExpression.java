package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

public class TSLExpression extends TSLWord {

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
        return platform.getExpressionEvaluator().evaluate(expression);
    }

    public interface Evaluator {
        String evaluate(String expression);
    }

}
