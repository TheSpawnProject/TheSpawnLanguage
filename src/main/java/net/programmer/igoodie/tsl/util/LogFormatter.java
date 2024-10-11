package net.programmer.igoodie.tsl.util;

import java.util.regex.Pattern;

public final class LogFormatter {

    public static final Pattern ARG_PATTERN = Pattern.compile("\\{}");

    public static String format(String format, Object... args) {
        return PatternReplacer.replaceMatches(ARG_PATTERN, format,
                (matcher, matchIndex) -> args[matchIndex].toString());
    }

    public static String escapeJson(String jsonString) {
        StringBuilder escapedString = new StringBuilder();

        for (char character : jsonString.toCharArray()) {
            if (character == '\'' || character == '"' || character == '\\') {
                escapedString.append("\\");
            }

            escapedString.append(character);
        }

        return escapedString.toString();
    }

}
