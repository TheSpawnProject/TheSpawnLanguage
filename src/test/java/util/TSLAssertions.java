package util;

import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.util.StringUtils;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class TSLAssertions {

    public static void assertSnippetsEqual(List<String> lines, TSLSnippet<?> actual) {
        Assertions.assertEquals(
                "\n" + String.join("\n", lines) + "\n",
                "\n" + debugTree(actual, 0));
    }

    public static String debugTree(TSLSnippet<?> snippet, int depth) {
        StringBuilder sb = new StringBuilder();

        for (Either<TSLToken, TSLSnippet<?>> snippetEntry : snippet.getSnippetEntries()) {
            String tree = snippetEntry.fold(
                    token -> StringUtils.repeat(" ", depth) + (depth + 1) + "- " + token + "\n",
                    subSnippet -> debugTree(subSnippet, depth + 1)
            );
            sb.append(tree);
        }

        return sb.toString();
    }

}
