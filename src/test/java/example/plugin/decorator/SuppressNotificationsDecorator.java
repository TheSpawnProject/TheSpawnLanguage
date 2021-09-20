package example.plugin.decorator;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuppressNotificationsDecorator extends TSLDecorator {

    public static final SuppressNotificationsDecorator INSTANCE = new SuppressNotificationsDecorator();

    private SuppressNotificationsDecorator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "suppressNotifications");
    }

    @NotNull
    @Override
    public GoodieObject evaluateDecoratorAttributes(List<String> argument) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();
        attributes.put("notificationsMuted", true);
        return attributes;
    }

}
