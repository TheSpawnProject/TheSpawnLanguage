package example.setup.decorator;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.parser.token.TSLString;

import java.util.List;

public class SuppressNotificationsDecorator extends TSLDecorator {

    public static final SuppressNotificationsDecorator INSTANCE = new SuppressNotificationsDecorator();

    private SuppressNotificationsDecorator() {
        super("suppressNotifications");
    }

    @Override
    public JsonObject compose(TSLString tagName, List<TSLString> args) {
        return new JsonObject();
    }

}
