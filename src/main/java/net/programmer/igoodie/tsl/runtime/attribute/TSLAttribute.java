package net.programmer.igoodie.tsl.runtime.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import org.jetbrains.annotations.NotNull;

public class TSLAttribute<T> {

    private final @NotNull TSLPlugin plugin;
    private final @NotNull String attributeName;

    public TSLAttribute(@NotNull TSLPlugin plugin, @NotNull String attributeName) {
        this.plugin = plugin;
        this.attributeName = attributeName;
    }

    public String getId() {
        // TODO: Why, is this legacy?
        return plugin instanceof TSLGrammarCore
                ? attributeName : plugin.prependNamespace(attributeName);
    }

    public boolean isContained(GoodieObject attributes) {
        return attributes.containsKey(getId());
    }

    public void set(GoodieObject attributes, T value) {
        attributes.put(getId(), GoodieElement.from(value));
    }

}
