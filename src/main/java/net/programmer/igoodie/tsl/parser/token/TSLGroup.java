package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.Copyable;
import net.programmer.igoodie.tsl.parser.helper.ListBuilder;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLCaptureParameterFiller;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TSLGroup extends TSLToken implements TSLCaptureParameterFiller<TSLGroup> {

    protected String template;
    protected List<ExpressionToken> expressions;

    public TSLGroup(TextPosition beginPos, TextPosition endPos,
                    String template,
                    List<ExpressionToken> expressions) {
        super(beginPos, endPos);
        this.template = template;
        this.expressions = Collections.unmodifiableList(expressions);
    }

    @Override
    public TSLGroup copy() {
        return new TSLGroup(
                getBeginningPos(),
                getEndingPos(),
                template,
                Copyable.copyList(expressions));
    }

    public List<ExpressionToken> getExpressions() {
        return expressions;
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

    @Override
    public TSLGroup fillCaptureParameters(Map<String, TSLToken> arguments) {
        List<ExpressionToken> filledExpressions = new ArrayList<>(this.expressions);
        StringBuilder filledTemplate = new StringBuilder();

        for (int i = 0; i < filledExpressions.size(); i++) {
            ExpressionToken expression = filledExpressions.get(i);
            if (expression.encapsulatedToken instanceof TSLCaptureParameter) {
                TSLCaptureParameter parameter = (TSLCaptureParameter) expression.encapsulatedToken;
                TSLToken value = arguments.get(parameter.getParameterName());
                if (value != null) {
                    filledExpressions.set(i, new ExpressionToken(
                            expression.getBeginningPos(),
                            expression.getEndingPos(),
                            expression.beginOffset,
                            expression.endOffset,
                            value
                    ));
                }
            }
        }

        int cursor = 0;

        for (ExpressionToken expression : filledExpressions) {
            filledTemplate.append(template, cursor, expression.beginOffset);
            filledTemplate.append(expression.getRaw());
            cursor = expression.endOffset + 1;
        }

        filledTemplate.append(template, cursor, template.length());

        return new TSLGroup(
                range.getBeginPos(),
                range.getEndPos(),
                filledTemplate.toString(),
                filledExpressions
        );
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
        public ExpressionToken copy() {
            return new ExpressionToken(
                    getBeginningPos(),
                    getEndingPos(),
                    beginOffset,
                    endOffset,
                    encapsulatedToken.copy());
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

    public static class ExpressionListBuilder extends ListBuilder<ExpressionToken> {

        public ExpressionListBuilder(Supplier<List<ExpressionToken>> initializer) {
            super(initializer);
        }

    }

}
