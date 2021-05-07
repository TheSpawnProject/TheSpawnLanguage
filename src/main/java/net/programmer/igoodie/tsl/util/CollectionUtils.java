package net.programmer.igoodie.tsl.util;

import java.util.LinkedList;
import java.util.List;

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

}
