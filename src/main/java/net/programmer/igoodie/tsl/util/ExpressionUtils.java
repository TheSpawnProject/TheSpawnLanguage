package net.programmer.igoodie.tsl.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionUtils {

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.+?)}");
    public static final Pattern CAPTURE_PARAMETER_PATTERN = Pattern.compile("\\{\\{(.+?)}}");

    public static String replaceExpressions(String input, Function<String, String> evaluator) {
        return replacePattern(input, EXPRESSION_PATTERN, evaluator);
    }

    public static String replaceCaptureParams(String input, Function<String, String> evaluator) {
        return replacePattern(input, CAPTURE_PARAMETER_PATTERN, evaluator);
    }

    public static String replacePattern(String input, Pattern expressionPattern, Function<String, String> evaluator) {
        Matcher matcher = expressionPattern.matcher(input);
        StringBuilder builder = new StringBuilder();

        int start = 0;
        try {
            while (matcher.find()) {
                String expression = matcher.group(1);

                // Append previous part
                builder.append(input, start, matcher.start());
                start = matcher.end();

                // Evaluate and append new value
                builder.append(evaluator.apply(expression));
            }

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Pattern must have a group inside", e);
        }

        // Append trailing chars
        builder.append(input, start, input.length());

        return builder.toString();
    }

}
