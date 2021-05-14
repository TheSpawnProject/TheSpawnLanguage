package example.setup.decorator;

import com.google.gson.JsonObject;
import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuppressNotificationsDecorator extends TSLDecorator {

    public static final SuppressNotificationsDecorator INSTANCE = new SuppressNotificationsDecorator();

    private SuppressNotificationsDecorator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "suppressNotifications");
    }

    @NotNull
    @Override
    public JsonObject evaluateDecoratorAttributes(List<String> argument) throws TSLRuntimeError {
        JsonObject attributes = new JsonObject();
        attributes.addProperty("notificationsMuted", true);
        return attributes;
    }

}
