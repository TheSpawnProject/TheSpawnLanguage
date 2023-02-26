package net.programmer.igoodie.tsl.plugin.extension;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.util.ValueHolder;
import org.pf4j.ExtensionPoint;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface TSLDefinitionsEP extends ExtensionPoint {

    default <T> List<ValueHolder<T>> createDefinitionList(T... value) {
        return Stream.of(value).map(ValueHolder::of)
                .collect(Collectors.toList());
    }

    default void registerDefinitions(TheSpawnLanguage tsl) {
        getTags().stream().map(ValueHolder::getValue).forEach(tsl.TAG_REGISTRY::register);
        getDecorators().stream().map(ValueHolder::getValue).forEach(tsl.DECORATOR_REGISTRY::register);
        getEvents().stream().map(ValueHolder::getValue).forEach(tsl.EVENT_REGISTRY::register);
        getActions().stream().map(ValueHolder::getValue).forEach(tsl.ACTION_REGISTRY::register);
        getComparators().stream().map(ValueHolder::getValue).forEach(tsl.COMPARATOR_REGISTRY::register);
        getPredicates().stream().map(ValueHolder::getValue).forEach(tsl.PREDICATE_REGISTRY::register);
        getFunctionLibraries().stream().map(ValueHolder::getValue).forEach(tsl.FUNC_LIBRARY_REGISTRY::register);
    }

    default void unregisterDefinitions(TheSpawnLanguage tsl) {
        getTags().stream().map(ValueHolder::getValue).forEach(tsl.TAG_REGISTRY::unregister);
        getDecorators().stream().map(ValueHolder::getValue).forEach(tsl.DECORATOR_REGISTRY::unregister);
        getEvents().stream().map(ValueHolder::getValue).forEach(tsl.EVENT_REGISTRY::unregister);
        getActions().stream().map(ValueHolder::getValue).forEach(tsl.ACTION_REGISTRY::unregister);
        getComparators().stream().map(ValueHolder::getValue).forEach(tsl.COMPARATOR_REGISTRY::unregister);
        getPredicates().stream().map(ValueHolder::getValue).forEach(tsl.PREDICATE_REGISTRY::unregister);
        getFunctionLibraries().stream().map(ValueHolder::getValue).forEach(tsl.FUNC_LIBRARY_REGISTRY::unregister);
    }

    default List<ValueHolder<TSLTag>> getTags() {
        return Collections.emptyList();
    }

    default List<ValueHolder<TSLDecorator>> getDecorators() {
        return Collections.emptyList();
    }

    default List<ValueHolder<TSLEvent>> getEvents() {
        return Collections.emptyList();
    }

    default List<ValueHolder<TSLAction>> getActions() {
        return Collections.emptyList();
    }

    default List<ValueHolder<TSLComparator>> getComparators() {
        return Collections.emptyList();
    }

    default List<ValueHolder<TSLPredicate>> getPredicates() {
        return Collections.emptyList();
    }

    default List<ValueHolder<TSLFunctionLibrary>> getFunctionLibraries() {
        return Collections.emptyList();
    }

}
