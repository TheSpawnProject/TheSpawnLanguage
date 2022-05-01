package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
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
        // TODO: Replace with a lexer logic. Will break for --> %${"${not-an-expression}"}%
        return ExpressionUtils.replaceExpressions(group, (expression) ->
                context.getTsl().getJsEngine().evaluate(expression, context));
    }

}
