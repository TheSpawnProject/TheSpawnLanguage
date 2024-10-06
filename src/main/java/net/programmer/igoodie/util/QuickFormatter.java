package net.programmer.igoodie.util;

import java.util.regex.Pattern;

public class QuickFormatter {

    public static final Pattern ARG_PATTERN = Pattern.compile("\\{}");

    public static String replaceArgs(String msg, Object... args) {
        return PatternReplacer.replaceMatches(ARG_PATTERN, msg,
                (matcher, matchIndex) -> args[matchIndex].toString());
    }

}
