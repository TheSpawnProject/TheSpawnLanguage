package net.programmer.igoodie.tsl.util;

public class StringUtils {

    public static int occurrenceCount(String text, char character) {
        return (int) text.chars().filter(ch -> ch == character).count();
    }

}
