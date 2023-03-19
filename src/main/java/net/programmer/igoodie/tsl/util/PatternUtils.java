package net.programmer.igoodie.tsl.util;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

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

        int cursor = 0;
        try {
            while (matcher.find()) {
                int matchBegin = matcher.start();
                String expression = matcher.group(1);
                boolean isEscaped = matchBegin != 0 && input.charAt(matchBegin - 1) == '\\';

                // Append previous part
                builder.append(input, cursor, isEscaped ? matchBegin - 1 : matchBegin);
                cursor = matcher.end();

                // Evaluate and append new value
                builder.append(isEscaped
                        ? matcher.group(0) // Do not evaluate, if escaped
                        : transformer.transform(expression, matcher));
            }

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Pattern must have a group inside", e);
        }

        // Append trailing chars
        builder.append(input, cursor, input.length());

        return builder.toString();
    }

    public static void traverseMatches(String input, Pattern expressionPattern, BiConsumer<Integer, Integer> rangeConsumer) {
        Matcher matcher = expressionPattern.matcher(input);
        while (matcher.find()) {
            int matchBegin = matcher.start();
            boolean isEscaped = matchBegin != 0 && input.charAt(matchBegin - 1) == '\\';
            if (!isEscaped) rangeConsumer.accept(matcher.start(), matcher.end());
        }
    }

    // ${"${}"} --> ${}
    // ${{{x}} + {{y}}} --> ${1 + 2} --> 3
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
