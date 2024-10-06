package net.programmer.igoodie.util;

import java.util.regex.Pattern;

public final class LogFormatter {

    public static final Pattern ARG_PATTERN = Pattern.compile("\\{}");

    public static String format(String format, Object... args) {
        return PatternReplacer.replaceMatches(ARG_PATTERN, format,
                (matcher, matchIndex) -> args[matchIndex].toString());
    }

}
