package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

public class TSLExpression extends TSLToken {

    protected String expression;

    public TSLExpression(int line, int character, String expression) {
        super(line, character);
        this.expression = expression;
    }

    @Override
    public String getRaw() {
        return "${" + expression + "}";
    }

    @Override
    public String evaluate(TSLContext context) {
        // TODO: Evaluate with JS engine
        return expression;
    }

}
