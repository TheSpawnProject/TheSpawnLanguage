package net.programmer.igoodie.tsl.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ValuePipe<T> {

    private static final ValuePipe<?> EMPTY = new ValuePipe<>();

    protected T value;

    private ValuePipe() {}

    private ValuePipe(T value) {
        this.value = value;
    }

    public ValuePipe<T> filter(Function<T, Boolean> predicate) {
        if (!predicate.apply(value)) {
            return empty();
        }
        return this;
    }

    public <R> ValuePipe<R> map(Function<T, R> mapper) {
        return new ValuePipe<>(mapper.apply(value));
    }

    public ValuePipe<T> consume(Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

    public T getValue() {
        return value;
    }

    public Optional<T> optional() {
        return Optional.ofNullable(value);
    }

    /* ------------------------- */

    public static <T> ValuePipe<T> empty() {
        @SuppressWarnings("unchecked") ValuePipe<T> t = (ValuePipe<T>) EMPTY;
        return t;
    }

    public static <T> ValuePipe<T> of(T value) {
        return new ValuePipe<>(value);
    }

}
