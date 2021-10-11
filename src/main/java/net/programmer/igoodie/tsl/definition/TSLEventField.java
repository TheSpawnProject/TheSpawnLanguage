package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;

public abstract class TSLEventField<T> extends TSLDefinition implements TSLRegistrable {

    public TSLEventField(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    /* ---------------------------------- */

    public abstract T extractValue(GoodieObject goodie);

}
