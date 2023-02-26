package net.programmer.igoodie.tsl.util;

import java.util.function.Supplier;

public class ValueHolder<V> {

    protected V value;

    private ValueHolder() {}

    public V getValue() {
        return value;
    }

    /* --------------------- */

    public static <V> ValueHolder<V> supplier(Supplier<V> supplier) {
        return new ValueHolder<V>() {
            @Override
            public V getValue() {
                return supplier.get();
            }
        };
    }

    public static <V> ValueHolder<V> lazy(Supplier<V> lazySupplier) {
        return new ValueHolder<V>() {
            @Override
            public V getValue() {
                if (this.value == null)
                    this.value = lazySupplier.get();
                return super.getValue();
            }
        };
    }

    public static <V> ValueHolder<V> of(V value) {
        ValueHolder<V> holder = new ValueHolder<>();
        holder.value = value;
        return holder;
    }

}
