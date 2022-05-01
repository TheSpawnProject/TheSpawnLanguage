package example.plugin.tag;

import example.plugin.ExampleAttributes;
import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CooldownTag extends TSLTag {

    public static final CooldownTag INSTANCE = new CooldownTag();

    private CooldownTag() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "COOLDOWN");
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();

        if (arguments.size() < 1)
            throw new TSLSyntaxError("Expected cooldown duration.", tagName);

        TSLToken durationToken = arguments.get(0);
        double duration = parseDouble(durationToken, context);

        ExampleAttributes.COOLDOWN_DURATION.set(attributes, duration);

        return attributes;
    }

}