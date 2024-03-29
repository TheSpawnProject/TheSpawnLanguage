package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.function.format.NativeJsObjectGoodieFormat;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TSLEvent extends TSLDefinition {

    private static final NativeJsObjectGoodieFormat NATIVE_JS_OBJECT_GOODIE_FORMAT = new NativeJsObjectGoodieFormat();

    public TSLEvent(TSLPlugin plugin, String name) {
        super(plugin, StringUtilities.upperFirstLetters(name));
    }

    @Override
    public final @Nullable List<Couple<String, String>> getCompletionSnippets() {
        return null; // Events do not have a completion snippet
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

    public static @Nullable Object extractField(GoodieObject eventArguments, String fieldName) {
        if (eventArguments.hasNumber(fieldName))
            return eventArguments.getNumber(fieldName).orElse(-1);

        if (eventArguments.hasBoolean(fieldName))
            return eventArguments.getBoolean(fieldName).orElse(false);

        if (eventArguments.hasString(fieldName))
            return eventArguments.getString(fieldName).orElse(null);

        return eventArguments.get(fieldName);
    }

    public static NativeObject generateEventJsObject(TSLContext context) {
        return generateEventJsObject(context.getEventArguments());
    }

    public static NativeObject generateEventJsObject(GoodieObject eventArguments) {
        NativeObject jsObject = new NativeObject();

        for (String argumentName : eventArguments.keySet()) {
            Object value = extractField(eventArguments, argumentName);

            if (value instanceof GoodieObject)
                value = NATIVE_JS_OBJECT_GOODIE_FORMAT.readFromGoodie(((GoodieObject) value));

            jsObject.defineProperty(argumentName, value, ScriptableObject.CONST);
        }

        return jsObject;
    }

}
