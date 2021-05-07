package util;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestFiles {

    @SuppressWarnings("UnstableApiUsage")
    public static String loadTSLScript(String filename) throws IOException {
        URL resourceURL = Resources.getResource("tsl/" + filename);
        List<String> lines = Resources.readLines(resourceURL, StandardCharsets.UTF_8);
        return String.join("\n", lines);
    }

}
