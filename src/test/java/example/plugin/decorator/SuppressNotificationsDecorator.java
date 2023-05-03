package example.plugin.decorator;

import example.plugin.ExampleAttributes;
import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuppressNotificationsDecorator extends TSLDecorator {

    public static final SuppressNotificationsDecorator INSTANCE = new SuppressNotificationsDecorator();

    private SuppressNotificationsDecorator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "suppressNotifications");
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, List<TSLToken> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();
        ExampleAttributes.NOTIFICATIONS_SUPPRESSED.set(attributes, true);
        return attributes;
    }

}
