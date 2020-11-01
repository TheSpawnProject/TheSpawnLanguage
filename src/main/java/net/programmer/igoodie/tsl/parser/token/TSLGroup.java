package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

public class TSLGroup extends TSLToken {

    protected String group;

    public TSLGroup(int line, int character, String group) {
        super(line, character);
        this.group = group;
    }

    @Override
    public String getRaw() {
        return "%" + group + "%";
    }

    @Override
    public String evaluate(TSLContext context) {
        return TSLExpression.replaceExpressions(group, (expression) ->
                context.getEngine().evaluate(expression));
    }

}
