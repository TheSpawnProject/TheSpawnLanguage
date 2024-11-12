package net.programmer.igoodie.tsl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Utils {

    private Utils() {}

    public static <V> List<List<V>> splitIntoChunks(List<V> input, Predicate<V> delimiterPredicate) {
        List<List<V>> result = new ArrayList<>();
        List<V> currentList = new ArrayList<>();

        for (V element : input) {
            if (delimiterPredicate.test(element)) {
                result.add(new ArrayList<>(currentList));
                currentList.clear();
            } else {
                currentList.add(element);
            }
        }

        if (!currentList.isEmpty()) {
            result.add(currentList);
        }

        return result;
    }

    public static String leftPad(String str, int desiredLength, char padChar) {
        return repeat(Character.toString(padChar), Math.max(0, desiredLength - str.length())) + str;
    }

    public static String rightPad(String str, int desiredLength, char padChar) {
        return str + repeat(Character.toString(padChar), Math.max(0, desiredLength - str.length()));
    }

    public static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

}
