package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.tsl.plugin.TSLPlugin;

public abstract class TSLDecorator extends TSLAttributeGenerator {

    public TSLDecorator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

}
