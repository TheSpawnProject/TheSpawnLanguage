package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.RhinoException;

public class TSLExpression extends TSLToken {

    protected String expression;

    public TSLExpression(int line, int character, String expression) {
        super(line, character);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String getTypeName() {
        return "Expression";
    }

    @Override
    public String getRaw() {
        return "${" + expression + "}";
    }

    @Override
    public String evaluate(TSLContext context) {
        try {
            JSEngine jsEngine = context.getTsl().getJsEngine();
            return jsEngine.evaluate(expression, context);

        } catch (RhinoException | TSLExpressionException error) {
            error.printStackTrace();
            return "#!ERROR!#";
        }
    }

}
