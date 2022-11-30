package net.programmer.igoodie.tsl.util;

import java.util.Optional;

public class OptionalUtils {

    @FunctionalInterface
    public interface ErrorThrowingSupplier<T, E extends Exception> {
        T get() throws E;
    }

    public static <T> Optional<T> create(ErrorThrowingSupplier<T, ?> generator) {
        try {
            T value = generator.get();
            return Optional.ofNullable(value);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
}
