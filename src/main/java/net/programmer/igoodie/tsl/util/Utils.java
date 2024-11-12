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

}
