package net.programmer.igoodie.tsl.util;

import java.util.Locale;

public class StringUtils {

    public static String[] splitWords(String sentence) {
        return sentence.split("\\s+");
    }

    public static String upperFirstLetters(String phrase) {
        StringBuilder builder = new StringBuilder();

        for (String word : splitWords(phrase)) {
            if (word.isEmpty()) continue;
            char firstLetter = Character.toUpperCase(word.charAt(0));
            String rest = allLower(word.substring(1));
            builder.append(firstLetter).append(rest).append(' ');
        }

        return builder.toString().trim();
    }

    public static String allUpper(String phrase) {
        return phrase.toUpperCase(Locale.ENGLISH);
    }

    public static String allLower(String phrase) {
        return phrase.toLowerCase(Locale.ENGLISH);
    }

}
