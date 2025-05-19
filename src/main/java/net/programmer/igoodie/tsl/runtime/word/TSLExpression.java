package net.programmer.igoodie.tsl.runtime.word;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.Objects;
import java.util.Optional;

public class TSLExpression extends TSLWord {

    protected final String script;

    public TSLExpression(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    @Override
    public String evaluate(TSLEventContext ctx) {
        TSLPlatform platform = ctx.getPlatform();

        Optional<String> result = platform.getExpressionEvaluators().stream()
                .map(evaluator -> evaluator.evaluate(script))
                .filter(Objects::nonNull)
                .findFirst();

        return result.orElseThrow(() ->
                new TSLPerformingException("Cannot evaluate "));
    }

    public interface Evaluator {
        String evaluate(String expression);
    }

}
