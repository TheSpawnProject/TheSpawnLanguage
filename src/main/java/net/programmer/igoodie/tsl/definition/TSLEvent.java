package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import net.programmer.igoodie.util.Couple;
import net.programmer.igoodie.util.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class TSLEvent extends TSLDefinition implements TSLRegistrable {

    public TSLEvent(TSLPlugin plugin, String name) {
        super(plugin, StringUtilities.upperFirstLetters(name));
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    /* ---------------------------------- */

    public abstract @NotNull Map<String, Class<?>> getAcceptedFields();

    @SafeVarargs
    protected final Map<String, Class<?>> eventFields(Couple<String, Class<?>>... fields) {
        Map<String, Class<?>> eventFields = new HashMap<>();
        for (Couple<String, Class<?>> field : fields) {
            eventFields.put(field.getFirst(), field.getSecond());
        }
        return eventFields;
    }

    public static Object extractField(GoodieObject eventArguments, String fieldName) {
        if (eventArguments.hasNumber(fieldName))
            return eventArguments.getNumber(fieldName).orElse(-1);
        if (eventArguments.hasBoolean(fieldName))
            return eventArguments.getBoolean(fieldName).orElse(false);
        return eventArguments.getString(fieldName);
    }

}
