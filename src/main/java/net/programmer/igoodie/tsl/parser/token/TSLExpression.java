package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;
import org.mozilla.javascript.EcmaError;

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
            return context.getLanguage().getJsEngine().evaluate(expression, context);
        } catch (EcmaError error) {
            return "#!ERROR!#";
        }
    }

}
