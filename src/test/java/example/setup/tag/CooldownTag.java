package example.setup.tag;

import com.google.gson.JsonObject;
import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;

import java.util.List;

public class CooldownTag extends TSLTag {

    public static final CooldownTag INSTANCE = new CooldownTag();

    private CooldownTag() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "COOLDOWN");
    }

    @Override
    public JsonObject evaluateAttributes(TSLString tagName, List<TSLString> args) {
        JsonObject json = new JsonObject();

        if (args.size() < 1)
            throw new TSLSyntaxError("Expected cooldown duration.", tagName);

        TSLString cooldownDuration = args.get(0);

        json.addProperty("cooldown_duration", parseDouble(cooldownDuration));

        return json;
    }

}