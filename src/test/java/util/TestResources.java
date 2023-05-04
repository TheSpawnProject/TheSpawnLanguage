package util;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestResources {

    public static String constructTSLScript(String... lines) {
        return String.join("\n", lines);
    }

    public static String loadTSLScript(String filename) throws IOException {
        return loadResource("tsl/" + filename);
    }

    public static String loadFragment(String filename) throws IOException {
        return loadResource("fragments/" + filename);
    }

    public static String loadJS(String filename) throws IOException {
        return loadResource("rhino/" + filename);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static URL pluginURL(String pluginName) {
        return Resources.getResource("plugins/" + pluginName);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static URL scriptURL(String scriptName) {
        return Resources.getResource("tsl/" + scriptName);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static URL resourceURL(String resourceName) {
        return Resources.getResource(resourceName);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String loadResource(String path) throws IOException {
        URL resourceURL = Resources.getResource(path);
        List<String> lines = Resources.readLines(resourceURL, StandardCharsets.UTF_8);
        return String.join("\n", lines);
    }

}
