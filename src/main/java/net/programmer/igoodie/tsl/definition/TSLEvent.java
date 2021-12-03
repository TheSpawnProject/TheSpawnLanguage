package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import net.programmer.igoodie.util.StringUtilities;

import java.util.Arrays;
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

    // TODO: turn into Set<Couple<String, Class<?>>>
    public abstract Set<String> getAcceptedFields();

    protected Set<String> eventFields(String... fields) {
        return new HashSet<>(Arrays.asList(fields));
    }

    public static Object extractField(GoodieObject eventArguments, String fieldName) {
        if (eventArguments.hasNumber(fieldName))
            return eventArguments.getNumber(fieldName).orElse(-1);
        if (eventArguments.hasBoolean(fieldName))
            return eventArguments.getBoolean(fieldName).orElse(false);
        return eventArguments.getString(fieldName);
    }

}
