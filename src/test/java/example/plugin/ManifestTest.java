package example.plugin;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class ManifestTest {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
    }

    @Test
    public void loadingViaManifestTest() {
        try {
            Class<?> pluginClass = Class.forName("example.plugin.ExamplePlugin");

            Object plugin = pluginClass.getDeclaredConstructor().newInstance();

            if (plugin instanceof TSLPlugin) {
                TSL.loadPlugin((TSLPlugin) plugin);

            } else {
                throw new IllegalArgumentException("example.setup.ExamplePlugin is not a TSL plugin.");
            }

            System.out.println(TSL.LOADED_PLUGINS);

        } catch (ClassNotFoundException e) {
            System.out.println("Unknown class name");
            e.printStackTrace();

        } catch (NoSuchMethodException e) {
            System.out.println("Plugin's default constructor is missing.");
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            System.out.println("Plugin's constructor must be public.");
            e.printStackTrace();

        } catch (InstantiationException e) {
            System.out.println("Plugin MUST not be an abstract class.");
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            System.out.println("Plugin's constructor threw an exception");
            e.printStackTrace();

        }
    }

}
