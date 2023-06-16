package net.programmer.igoodie.tsl.exception.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessageFormatter {

    public static String format(String format, Object... args) {
        Pattern pattern = Pattern.compile("\\{}");
        Matcher matcher = pattern.matcher(format);
        StringBuilder builder = new StringBuilder();

        int cursor = 0;
        int argIndex = 0;

        while (matcher.find()) {
            int start = matcher.start();

            // Append prev part
            builder.append(format, cursor, start);

            // Append argument
            builder.append(args[argIndex++]);

            // Move the cursor
            cursor = matcher.end();
        }

        // Append trailing chars
        builder.append(format, cursor, format.length());

        return builder.toString();
    }

}
