package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;

public class TSLAttribute {

    public final TSLPlugin plugin;
    public final String attributeName;

    public TSLAttribute(TSLPlugin plugin, String attributeName) {
        this.plugin = plugin;
        this.attributeName = attributeName;
    }

    public String getId() {
        return plugin instanceof TSLGrammarCore
                ? attributeName : plugin.prependNamespace(attributeName);
    }

    public boolean isContained(GoodieObject attributes) {
        return attributes.containsKey(getId());
    }

    public void set(GoodieObject attributes, Object value) {
        attributes.put(getId(), GoodieElement.from(value));
    }

}
