package example.setup.decorator;

import com.google.gson.JsonObject;
import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.parser.token.TSLString;

import java.util.List;

public class SuppressNotificationsDecorator extends TSLDecorator {

    public static final SuppressNotificationsDecorator INSTANCE = new SuppressNotificationsDecorator();

    private SuppressNotificationsDecorator() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "suppressNotifications");
    }

    @Override
    public JsonObject evaluateAttributes(TSLString tagToken, List<TSLString> args) {
        JsonObject attributes = new JsonObject();

        attributes.addProperty("notificationsMuted", true);

        return attributes;
    }

}
