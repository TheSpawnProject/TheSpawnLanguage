package net.programmer.igoodie.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternReplacer {

    @FunctionalInterface
    public interface ReplacerFunc {
        String getReplacement(Matcher matcher, int matchIndex);
    }

    public static String replaceMatches(Pattern pattern, String input, ReplacerFunc replacer) {
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();
        int start = 0;
        int matchIndex = 0;

        while (matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();

            // Append previous part
            sb.append(input, start, matchStart);
            start = matchEnd;

            // Evaluate and append new value
            sb.append(replacer.getReplacement(matcher, matchIndex++));

            if (matchEnd != matcher.end() || matchStart != matcher.start())
                throw new IllegalStateException("matcher::find should not be called within the replacer function.");
        }

        // Append trailing chars
        sb.append(input, start, input.length());

        return sb.toString();
    }

}
