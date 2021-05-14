package automated;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.util.GsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTests {

    @Test
    public void shouldMapKeys() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("foo", 1);
        jsonObject.addProperty("bar", 2);
        jsonObject.addProperty("baz", 2);

        JsonObject mappedObject = GsonUtils.mapKeys(jsonObject, key -> "prefix:" + key);

        for (String key : mappedObject.keySet()) {
            Assertions.assertTrue(key.startsWith("prefix:"));
        }
    }

}
