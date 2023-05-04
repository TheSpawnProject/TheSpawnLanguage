package net.programmer.igoodie.tsl.util;

import java.util.Optional;

public class TSLReflectionUtils {

    public static <T> Optional<T> castToClass(Class<T> type, Object obj) {
        if (!type.isInstance(obj)) return Optional.empty();
        return Optional.of(type.cast(obj));
    }

    public static <T> Optional<T> castToGeneric(Object obj) {
        try {
            @SuppressWarnings("unchecked")
            T casted = (T) obj;
            return Optional.ofNullable(casted);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
