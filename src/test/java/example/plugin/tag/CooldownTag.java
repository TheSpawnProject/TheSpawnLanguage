package example.plugin.tag;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CooldownTag extends TSLTag {

    public static final CooldownTag INSTANCE = new CooldownTag();

    private CooldownTag() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "COOLDOWN");
    }

    @NotNull
    @Override
    public GoodieObject evaluateTagAttributes(TSLString tagName, List<TSLString> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();

        if (arguments.size() < 1)
            throw new TSLSyntaxError("Expected cooldown duration.", tagName);

        TSLString cooldownDuration = arguments.get(0);

        attributes.put("cooldown_duration", parseDouble(cooldownDuration));

        return attributes;
    }

}