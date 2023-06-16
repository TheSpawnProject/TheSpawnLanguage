package parser;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TSLAssertions;

import java.util.Arrays;
import java.util.List;

public class LexerTests {

    @Test
    public void testSimpleSnippet() {
        String script = "FOO BAR BAZ (HEY THERE)";
        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        List<TSLUnparsedSnippet> snippets = lexer.getSnippets();

        Assertions.assertEquals(1, snippets.size());
        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLPlainWord(FOO) @ (L0:0 | L0:2)",
                "1- TSLPlainWord(BAR) @ (L0:4 | L0:6)",
                "1- TSLPlainWord(BAZ) @ (L0:8 | L0:10)",
                " 2- TSLPlainWord(HEY) @ (L0:13 | L0:15)",
                " 2- TSLPlainWord(THERE) @ (L0:17 | L0:21)"
        ), snippets.get(0));
    }

}
