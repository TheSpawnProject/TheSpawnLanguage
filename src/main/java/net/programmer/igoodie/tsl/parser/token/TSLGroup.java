package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.ListBuilder;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class TSLGroup extends TSLToken {

    protected String template;
    protected List<ExpressionToken> expressions;

    public TSLGroup(TextPosition beginPos, TextPosition endPos,
                    String template,
                    List<ExpressionToken> expressions) {
        super(beginPos, endPos);
        this.template = template;
        this.expressions = expressions;
    }

    @Override
    public @NotNull String getTokenType() {
        return "Group";
    }

    @Override
    public @NotNull String getRaw() {
        return "%" + template + "%";
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return TSLReflectionUtils.castToClass(TSLGroup.class, otherToken)
                .filter(that -> that.getRaw().equals(this.getRaw()))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        if (expressions.size() == 0) return template;

        int cursor = 0;
        StringBuilder builder = new StringBuilder();

        for (ExpressionToken expression : expressions) {
            builder.append(template, cursor, expression.beginOffset);
            builder.append(expression.evaluate(context));
            cursor = expression.endOffset + 1;
        }

        builder.append(template, cursor, template.length());

        return builder.toString();
    }

    /* ------------------------- */

    public static class ExpressionToken extends TSLToken {

        protected int beginOffset, endOffset;
        protected TSLToken encapsulatedToken;

        public ExpressionToken(TextPosition beginPos, TextPosition endPos, int beginOffset, int endOffset, TSLToken encapsulatedToken) {
            super(beginPos, endPos);
            this.beginOffset = beginOffset;
            this.endOffset = endOffset;
            this.encapsulatedToken = encapsulatedToken;
        }

        @Override
        public @NotNull String getTokenType() {
            return "Group Template Expression";
        }

        @Override
        public @NotNull String getRaw() {
            return "|" + encapsulatedToken.getRaw() + "|";
        }

        @Override
        public boolean equalValues(TSLToken otherToken) {
            return TSLReflectionUtils.castToClass(ExpressionToken.class, otherToken)
                    .filter(that -> that.encapsulatedToken.equalValues(this.encapsulatedToken))
                    .isPresent();
        }

        @Override
        public @NotNull String evaluate(TSLContext context) {
            return encapsulatedToken.evaluate(context);
        }

    }

    public static class TemplateVariableListBuilder extends ListBuilder<ExpressionToken> {

        public TemplateVariableListBuilder(Supplier<List<ExpressionToken>> initializer) {
            super(initializer);
        }

    }

}
