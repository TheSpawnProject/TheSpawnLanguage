package automated;

import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLExpression;
import net.programmer.igoodie.tsl.parser.token.TSLGroup;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LexerTests {

    private String repeat(String target, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(target);
        }
        return builder.toString();
    }

    private String joinKeywords(String... keywords) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keywords.length; i++) {
            builder.append(keywords[i]).append(",").append(repeat(" ", i));
        }
        return builder.toString();
    }

    @Test
    public void shouldLexRulesets() throws IOException {
        String script = TestUtils.loadTSLScript("lexer.test.tsl");

        TSLLexer lexer = new TSLLexer(script).lex();

        List<TSLSnippetBuffer> lexedSnippets = lexer.getSnippets();

        for (TSLSnippetBuffer snippet : lexedSnippets) {
            System.out.println(snippet.getType());
            for (TSLToken token : snippet.getTokens()) {
                System.out.println(token);
            }
            System.out.println();
        }
    }

    @Test
    public void shouldLexCommaDelimited() {
        String[] keywords = {
                "KEYWORD1",
                "NAMESPACE.KEYWORD2",
                "%GROUP HERE!%",
                "${some + expression + Math.random()}",
                "@decorator(${foo(1, 2, 3, 4)}, %Foo, Bar, Baz!%)"
        };

        String script = joinKeywords(keywords);

        TSLLexer lexer = new TSLLexer(script).useCommaDelimiter().lex();

        List<TSLSnippetBuffer> lexedSnippets = lexer.getSnippets();
        Assertions.assertEquals(1, lexedSnippets.size());

        System.out.println();
        System.out.println(script);
        System.out.println();
        for (TSLSnippetBuffer snippet : lexedSnippets) {
            System.out.println(snippet.getType());
            for (TSLToken token : snippet.getTokens()) {
                System.out.println(token);
            }
            System.out.println();
        }

        List<TSLToken> tokens = lexedSnippets.get(0).getTokens();
        Assertions.assertIterableEquals(Arrays.asList(keywords),
                tokens.stream().map(TSLToken::getRaw).collect(Collectors.toList()));

        Assertions.assertEquals(TSLString.class, tokens.get(0).getClass());
        Assertions.assertEquals(TSLString.class, tokens.get(1).getClass());
        Assertions.assertEquals(TSLGroup.class, tokens.get(2).getClass());
        Assertions.assertEquals(TSLExpression.class, tokens.get(3).getClass());
    }

}
