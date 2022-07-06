package net.programmer.igoodie.tsl.util;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionUtils {

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.+?)}");
    public static final Pattern CAPTURE_PARAMETER_PATTERN = Pattern.compile("\\{\\{(.+?)}}");

    @FunctionalInterface
    public interface ExpressionTransformer {
        String transform(String expression, Matcher matcher);
    }

    public static String replaceExpressions(String input, Function<String, String> evaluator) {
        return replacePattern(input, EXPRESSION_PATTERN, evaluator);
    }

    public static String replaceCaptureParams(String input, Function<String, String> evaluator) {
        return replacePattern(input, CAPTURE_PARAMETER_PATTERN, evaluator);
    }

    @Deprecated
    public static String replacePattern(String input, Pattern expressionPattern, Function<String, String> evaluator) {
        return replacePattern(input, expressionPattern, (exp, matcher) -> evaluator.apply(exp));
    }

    public static String replacePattern(String input, Pattern expressionPattern, ExpressionTransformer transformer) {
        Matcher matcher = expressionPattern.matcher(input);
        StringBuilder builder = new StringBuilder();

        int start = 0;
        try {
            while (matcher.find()) {
                // TODO: Do not evaluate if escaped

                String expression = matcher.group(1);

                // Append previous part
                builder.append(input, start, matcher.start());
                start = matcher.end();

                // Evaluate and append new value
                builder.append(transformer.transform(expression, matcher));
            }

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Pattern must have a group inside", e);
        }

        // Append trailing chars
        builder.append(input, start, input.length());

        return builder.toString();
    }

    public static void traverseMatches(String input, Pattern expressionPattern, BiConsumer<Integer, Integer> rangeConsumer) {
        Matcher matcher = expressionPattern.matcher(input);
        while (matcher.find()) {
            rangeConsumer.accept(matcher.start(), matcher.end());
        }
    }

    // {{x}} \{{y}} --> x \{{y}}
    // ${x} \${y} --> x \${y}
    // ${"${}"} --> ${}
    public static String replaceX(String text, String begin) {
        return null; // TODO: Find a way to handle escapes in different contexts. Nuke this class if needed (?)
    }

    // TSL -> foo{{x}} # Invalid, should remain as plain word
    // TSL -> \${...} # Should remain plain word
    // TSL -> \{{x}} # Should remain plain word
    // TSL -> \% # Should remain plain word
    // TSL -> \(DROP apple) # Should remain plain words
    // TSL -> \\ # Should remain plain word
    // TSL -> \f # Should throw an "Invalid escape sequence" syntax error
    // TSL -> \\f # Should remain plain word
    // TSL -> 1\2 # Should throw an "Invalid escape sequence" syntax error
    // TSL -> 1\\2 # Should remain plain word

}
