package example.setup.fields;

import com.google.gson.JsonObject;
import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLEventField;

public class TimeField extends TSLEventField<Long> {

    public static final TimeField INSTANCE = new TimeField();

    private TimeField() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "time");
    }

    @Override
    public Long extractValue(JsonObject json) {
        return json.get(getName()).getAsLong();
    }

}
