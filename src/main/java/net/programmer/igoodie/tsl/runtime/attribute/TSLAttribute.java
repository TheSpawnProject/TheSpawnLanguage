package net.programmer.igoodie.tsl.runtime.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TSLAttribute<T> {

    private final @NotNull TSLPlugin plugin;
    private final @NotNull String attributeName;
    private final Function<GoodieElement, T> getter;

    public TSLAttribute(@NotNull TSLPlugin plugin, @NotNull String attributeName, Function<GoodieElement, T> getter) {
        this.plugin = plugin;
        this.attributeName = attributeName;
        this.getter = getter;
    }

    public String getId() {
        return plugin.getDescriptor().getPluginId() + ":" + attributeName;
    }

    public boolean isContained(GoodieObject attributes) {
        return attributes.containsKey(getId());
    }

    public void set(GoodieObject attributes, T value) {
        attributes.put(getId(), GoodieElement.from(value));
    }

    public T get(GoodieObject attributes) {
        return getter.apply(attributes.get(getId()));
    }

}
