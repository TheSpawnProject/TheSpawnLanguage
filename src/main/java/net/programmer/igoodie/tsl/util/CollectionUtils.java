package net.programmer.igoodie.tsl.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public final class CollectionUtils {

    private CollectionUtils() {}

    public static <T> List<T> asSpreadList(Class<T> type, Object... elements) {
        List<T> spreadList = new LinkedList<>();

        for (Object element : elements) {
            Class<?> elementType = element.getClass();

            if (type.isAssignableFrom(elementType)) {
                spreadList.add(type.cast(element));

            } else if (List.class.isAssignableFrom(elementType)) {
                spreadList.addAll(((List<T>) element));
            }
        }

        return spreadList;
    }

    public static <T> T findBy(List<T> list, Predicate<T> predicate) {
        int index = indexOfBy(list, predicate);
        if (index == -1) return null;
        return list.get(index);
    }

    public static <T> int indexOfBy(List<T> list, Predicate<T> predicate) {
        for (int i = 0; i < list.size(); i++) {
            T element = list.get(i);
            if (predicate.test(element)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> List<List<T>> splitBy(List<T> list, boolean includeDelimiter, Predicate<T> predicate) {
        List<List<T>> split = new LinkedList<>();
        List<T> buffer = new LinkedList<>();

        for (T current : list) {
            if (predicate.test(current)) {
                split.add(buffer);
                buffer = new LinkedList<>();
                if (includeDelimiter) {
                    buffer.add(current);
                }

            } else {
                buffer.add(current);
            }
        }

        split.add(buffer);
        return split;
    }

}
