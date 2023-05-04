package net.programmer.igoodie.tsl.parser.helper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ListBuilder<T> {

    protected List<T> list;

    public ListBuilder(Supplier<List<T>> initializer) {
        this.list = initializer.get();
    }

    public ListBuilder<T> addElement(T element) {
        list.add(element);
        return this;
    }

    @SafeVarargs
    public final ListBuilder<T> addElements(T... elements) {
        return addElements(Arrays.asList(elements));
    }

    public ListBuilder<T> addElements(List<T> elements) {
        list.addAll(elements);
        return this;
    }

    public List<T> build() {
        return list;
    }

}
