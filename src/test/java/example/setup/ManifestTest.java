package example.setup;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

public class ManifestTest {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
    }

    @Test
    public void loadingViaManifestTest() {
        try {
            Class<?> pluginClass = Class.forName("example.setup.ExamplePlugin");

            Object plugin = pluginClass.newInstance();

            if (plugin instanceof TSLPlugin) {
                TSL.loadPlugin((TSLPlugin) plugin);

            } else {
                throw new IllegalArgumentException("example.setup.ExamplePlugin is not a TSL plugin.");
            }

            System.out.println(TSL.LOADED_PLUGINS);

        } catch (ClassNotFoundException e) {
            System.out.println("Unknown class name");
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            System.out.println("Plugin's constructor must be public.");
            e.printStackTrace();

        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
