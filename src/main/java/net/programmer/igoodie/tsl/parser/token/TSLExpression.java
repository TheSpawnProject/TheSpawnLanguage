package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSLExpression extends TSLToken {

    protected String expression;

    public TSLExpression(int line, int character, String expression) {
        super(line, character);
        this.expression = expression;
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
        return context.getEngine().evaluate(expression, context);
    }

    /* ----------------------------------------------- */

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    public static String replaceExpressions(String input, Function<String, String> evaluator) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);
        StringBuilder builder = new StringBuilder();

        int start = 0;
        while (matcher.find()) {
            String expression = matcher.group(1);

            // Append previous part
            builder.append(input, start, matcher.start());
            start = matcher.end();

            // Evaluate and append new value
            builder.append(evaluator.apply(expression));
        }

        // Append trailing chars
        builder.append(input, start, input.length());

        return builder.toString();
    }

}
