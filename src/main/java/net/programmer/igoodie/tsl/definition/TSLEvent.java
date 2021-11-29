package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import net.programmer.igoodie.util.StringUtilities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class TSLEvent extends TSLDefinition implements TSLRegistrable {

    public TSLEvent(TSLPlugin plugin, String name) {
        super(plugin, StringUtilities.upperFirstLetters(name));
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    /* ---------------------------------- */

    public abstract Set<TSLEventField<?>> getAcceptedFields();

    /* ---------------------------------- */

    protected Set<TSLEventField<?>> eventFields(TSLEventField<?>... fields) {
        Set<TSLEventField<?>> set = new HashSet<>();
        Collections.addAll(set, fields);
        return set;
    }

}
