package net.programmer.igoodie.tsl.parser.helper;

import net.programmer.igoodie.tsl.util.TSLReflectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Copyable<T> {

    T copy();

    static <T extends Copyable<?>> List<T> copyUnmodifiableList(List<T> list) {
        return Collections.unmodifiableList(copyList(list));
    }

    static <T extends Copyable<?>> List<T> copyList(List<T> list) {
        return list.stream()
                .map(Copyable::copy)
                .map(TSLReflectionUtils::<T>castToGeneric)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
