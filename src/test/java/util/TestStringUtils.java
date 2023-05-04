package util;

public class TestStringUtils {

    public static String unescapeNewlines(String text) {
        return text.replaceAll("\\n", "\\\\n");
    }

}
