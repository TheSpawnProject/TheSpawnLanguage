package net.programmer.igoodie.tsl.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CollectionUtils {

    private CollectionUtils() {}

    public static <T> List<T> flatAll(Class<T> targetType, Object... elements) {
        return flatAll(targetType, Arrays.asList(elements));
    }

    public static <T> List<T> flatAll(Class<T> targetType, Collection<?> collection) {
        @SuppressWarnings("unchecked")
        List<T> flattened = (List<T>) collection.stream()
                .flatMap(element -> {

                    if (element == null) {
                        return Stream.empty();

                    } else if (element instanceof Collection) {
                        return flatAll(targetType, (Collection<?>) element).stream();

                    } else if (targetType.isAssignableFrom(element.getClass())) {
                        return Stream.of(element);

                    } else {
                        return Stream.empty(); // <-- targetType mismatch checked here
                    }
                })
                .collect(Collectors.toList());
        return flattened;
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

    public static <T> int lastIndexOfBy(List<T> list, Predicate<T> predicate) {
        for (int i = list.size() - 1; i >= 0; i--) {
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
