package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.util.ExpressionUtils;

public class TSLGroup extends TSLToken {

    protected String group;

    public TSLGroup(int line, int character, String group) {
        super(line, character);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String getTypeName() {
        return "Group";
    }

    @Override
    public String getRaw() {
        return "%" + group + "%";
    }

    @Override
    public String evaluate(TSLContext context) {
        return ExpressionUtils.replaceExpressions(group, (expression) ->
                context.getLanguage().getJsEngine().evaluate(expression, context));
    }

}
