package util;

public class TestUtils {

    /* String related */

    public static String unescapeNewlines(String text) {
        return text.replaceAll("\\n", "\\\\n");
    }

}
