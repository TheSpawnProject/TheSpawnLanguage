package net.programmer.igoodie.tsl.util;

public class StringUtils {

    public static String repeat(String str, int count) {
        StringBuilder repeat = new StringBuilder();
        for (int i = 0; i < count; i++) repeat.append(str);
        return repeat.toString();
    }

    public static int occurrenceCount(String text, char character) {
        return (int) text.chars().filter(ch -> ch == character).count();
    }

}
