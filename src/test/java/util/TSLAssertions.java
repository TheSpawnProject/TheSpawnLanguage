package util;

import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class TSLAssertions {

    public static void assertSnippetsEqual(List<String> lines, TSLSnippet<?> actual) {
        Assertions.assertEquals(
                "\n" + String.join("\n", lines) + "\n",
                "\n" + actual.toCanonicalDebugTree());
    }

}
