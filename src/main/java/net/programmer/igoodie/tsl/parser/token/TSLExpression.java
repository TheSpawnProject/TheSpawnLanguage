package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.mozilla.javascript.RhinoException;

// ${expr}
public class TSLExpression extends TSLToken {

    protected String expression;

    public TSLExpression(TextRange range, String expression) {
        super(range);
        this.expression = expression;
    }

    @Override
    public TSLExpression copy() {
        return new TSLExpression(range, expression);
    }

    @Override
    public @NotNull String getTokenType() {
        return "Expression";
    }

    @Override
    public @NotNull String getRaw() {
        return "${" + this.expression + "}";
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return TSLReflectionUtils.castToClass(TSLExpression.class, otherToken)
                .filter(that -> that.expression.equals(this.expression))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        try {
            JSEngine jsEngine = context.getTsl().getJsEngine();
            return jsEngine.evaluate(expression, context);

        } catch (RhinoException | TSLExpressionException error) {
            error.printStackTrace(); // <- TODO: Find a better way to handle this
            return "#!ERROR!#";
        }
    }

}
