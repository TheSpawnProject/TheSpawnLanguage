package example.plugin.decorator;

import example.plugin.ExampleAttributes;
import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CooldownDecorator extends TSLDecorator {

    public static final CooldownDecorator INSTANCE = new CooldownDecorator();

    private CooldownDecorator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "cooldown");
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, List<TSLToken> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();

        if (arguments.size() < 1)
            throw new TSLSyntaxError("Expected cooldown duration.");

        TSLToken durationToken = arguments.get(0);
        double duration = TSLArguments.parseDouble(durationToken, context)
                .orElseThrow(() -> new TSLRuntimeError("Expected a number for duration", durationToken));

        ExampleAttributes.COOLDOWN_DURATION.set(attributes, duration);

        return attributes;
    }

}
