package example.plugin.decorator;

import example.plugin.ExampleAttributes;
import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CooldownDecorator extends TSLDecorator {

    public static final CooldownDecorator INSTANCE = new CooldownDecorator();

    private CooldownDecorator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "cooldown");
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, List<String> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();

        if (arguments.size() < 1)
            throw new TSLSyntaxError("Expected cooldown duration.");

        String durationString = arguments.get(0);
        double duration = parseDouble(durationString);

        ExampleAttributes.COOLDOWN_DURATION.set(attributes, duration);

        return attributes;
    }

}
