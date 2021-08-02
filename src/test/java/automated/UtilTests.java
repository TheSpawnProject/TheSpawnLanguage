package automated;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTests {

    @Test
    public void shouldMapKeys() {
        GoodieObject goodie = new GoodieObject();
        goodie.put("foo", 1);
        goodie.put("bar", 2);
        goodie.put("baz", 2);

        GoodieObject mappedObject = GoodieUtils.mapKeys(goodie, key -> "prefix:" + key);

        for (String key : mappedObject.keySet()) {
            Assertions.assertTrue(key.startsWith("prefix:"));
        }
    }

}
