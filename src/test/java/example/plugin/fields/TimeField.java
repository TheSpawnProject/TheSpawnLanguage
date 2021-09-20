package example.plugin.fields;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLEventField;

public class TimeField extends TSLEventField<Long> {

    public static final TimeField INSTANCE = new TimeField();

    private TimeField() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "time");
    }

    @Override
    public Long extractValue(GoodieObject goodie) {
        return goodie.get(getName()).asPrimitive().getLong();
    }

}
