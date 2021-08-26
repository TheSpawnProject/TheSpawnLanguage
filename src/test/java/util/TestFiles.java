package util;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestFiles {

    public static String loadTSLScript(String filename) throws IOException {
        return loadFile("tsl/" + filename);
    }

    public static String loadFragment(String filename) throws IOException {
        return loadFile("fragments/" + filename);
    }

    public static String loadJS(String filename) throws IOException {
        return loadFile("rhino/" + filename);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static String loadFile(String path) throws IOException {
        URL resourceURL = Resources.getResource(path);
        List<String> lines = Resources.readLines(resourceURL, StandardCharsets.UTF_8);
        return String.join("\n", lines);
    }

}
