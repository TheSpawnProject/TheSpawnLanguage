package automated;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.plugin.TSLPluginLoader;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PluginLoaderTests {

    @Test
    public void test() throws IOException, URISyntaxException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        URI pluginURL = TestUtils.pluginPath("example.jar").toURI();

        TSLPluginLoader pluginLoader = new TSLPluginLoader(tsl, pluginURL);

        System.out.println("Begin loading");

        pluginLoader.load();
    }

}
