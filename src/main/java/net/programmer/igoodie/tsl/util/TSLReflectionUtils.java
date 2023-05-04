package net.programmer.igoodie.tsl.util;

import java.util.Optional;

public class TSLReflectionUtils {

    public static  <T> Optional<T> castToClass(Class<T> type, Object obj) {
        if (!type.isInstance(obj)) return Optional.empty();
        return Optional.of(type.cast(obj));
    }

}
