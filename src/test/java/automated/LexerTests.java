package automated;

import net.programmer.igoodie.legacy.parser.token.*;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.legacy.parser.TSLTokenBuffer;
import net.programmer.igoodie.legacy.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.runtime.TSLContext;
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

    private TSLToken lexSingle(String text) {
        TSLLexer lexer = new TSLLexer(text).lex();
        if (lexer.getSnippets().size() == 0) return null;
        return lexer.getSnippets().get(0).getTokens().get(0);
    }

    private TSLTokenBuffer lexTokens(String text) {
        TSLLexer lexer = new TSLLexer(text);
        List<TSLTokenBuffer> snippets = lexer.lex().getSnippets();
        if (snippets.size() == 0) throw new InternalError();
        return snippets.get(0);
    }

    @Test
    public void shouldLexRulesets() throws IOException {
        String script = TestUtils.loadTSLScript("lexer.test.tsl");

        TSLLexer lexer = new TSLLexer(script).lex();

        List<TSLTokenBuffer> lexedSnippets = lexer.getSnippets();

        System.out.println();
        for (TSLTokenBuffer snippet : lexedSnippets) {
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
                "@decorator(${foo(1, 2, 3, 4)}, %Foo, Bar, Baz!%)",
                "$concat(ABC, DEF)"
        };

        String script = joinKeywords(keywords);

        TSLLexer lexer = new TSLLexer(script).useCommaDelimiter().lex();

        List<TSLTokenBuffer> lexedSnippets = lexer.getSnippets();
        Assertions.assertEquals(1, lexedSnippets.size());

        System.out.println();
        System.out.println(script);
        System.out.println();
        for (TSLTokenBuffer snippet : lexedSnippets) {
            System.out.println(snippet.getType());
            for (TSLToken token : snippet.getTokens()) {
                System.out.println(token);
            }
            System.out.println();
        }

        List<TSLToken> tokens = lexedSnippets.get(0).getTokens();
        Assertions.assertIterableEquals(Arrays.asList(keywords),
                tokens.stream().map(TSLToken::getRaw).collect(Collectors.toList()));

        Assertions.assertEquals(TSLPlainWord.class, tokens.get(0).getClass());
        Assertions.assertEquals(TSLPlainWord.class, tokens.get(1).getClass());
        Assertions.assertEquals(TSLGroup.class, tokens.get(2).getClass());
        Assertions.assertEquals(TSLExpression.class, tokens.get(3).getClass());
    }

    @Test
    public void shouldLexIntoCorrectTypes() {
        Assertions.assertDoesNotThrow(() -> {
            String raw = "%a group containing percent (\\%) sign%";
            TSLToken token = lexSingle(raw);
            Assertions.assertNotNull(token);
            Assertions.assertEquals(TSLGroup.class, token.getClass());
            Assertions.assertEquals(raw, token.getRaw());
        });
        Assertions.assertDoesNotThrow(() -> {
            String raw = "@decorate(ABC, 123, %group!%, $capture, ${1 + 1 + {foo:1}.foo})";
            TSLToken token = lexSingle(raw);
            System.out.println(token);
            Assertions.assertNotNull(token);
            Assertions.assertEquals(TSLDecoratorCall.class, token.getClass());
            Assertions.assertEquals(raw, token.getRaw());
        });
        Assertions.assertDoesNotThrow(() -> {
            String raw = "$capture(${'ABC' + {bar:1}.bar}, ABC)";
            TSLToken token = lexSingle(raw);
            System.out.println(token);
            Assertions.assertNotNull(token);
            Assertions.assertEquals(TSLCaptureCall.class, token.getClass());
            Assertions.assertEquals(raw, token.getRaw());
        });
        Assertions.assertDoesNotThrow(() -> {
            String raw = "$mergeText(%Lemonades%, \\$5)";
            TSLToken token = lexSingle(raw);
            System.out.println(token);
            Assertions.assertNotNull(token);
            Assertions.assertEquals(TSLCaptureCall.class, token.getClass());
            Assertions.assertEquals(raw, token.getRaw());
        });
        Assertions.assertDoesNotThrow(() -> {
            String raw = "\\$fakeCapture";
            TSLToken token = lexSingle(raw);
            System.out.println(token);
            Assertions.assertNotNull(token);
            Assertions.assertEquals(TSLPlainWord.class, token.getClass());
            Assertions.assertEquals(raw, token.getRaw());
        });
    }

    @Test
    public void shouldLexEscapedTokens() {
        Assertions.assertDoesNotThrow(() -> {
            List<TSLToken> tokens = lexTokens("\\%90 foo bar \\%").getTokens();
            System.out.println(tokens);
            Assertions.assertEquals(4, tokens.size());
            Assertions.assertEquals("\\%90", tokens.get(0).getRaw());
            Assertions.assertEquals("foo", tokens.get(1).getRaw());
            Assertions.assertEquals("bar", tokens.get(2).getRaw());
            Assertions.assertEquals("\\%", tokens.get(3).getRaw());
        });
        Assertions.assertDoesNotThrow(() -> {
            List<TSLToken> tokens = lexTokens("\\${How much \\% actual tokens?}").getTokens();
            System.out.println(tokens);
            Assertions.assertEquals(5, tokens.size());
            Assertions.assertEquals("\\${How", tokens.get(0).getRaw());
            Assertions.assertEquals("much", tokens.get(1).getRaw());
            Assertions.assertEquals("\\%", tokens.get(2).getRaw());
            Assertions.assertEquals("actual", tokens.get(3).getRaw());
            Assertions.assertEquals("tokens?}", tokens.get(4).getRaw());
        });
    }

    @Test
    public void shouldPreserveSpacesInGroup() {
        String rawText = "%Tons    of   spaces ${'Hereee!'}%";
        TSLToken token = lexSingle(rawText);
        System.out.println(token);
        TSLContext dummyContext = new TSLContext(new TheSpawnLanguage.Bootstrapper("Test").bootstrap());
        Assertions.assertNotNull(token);
        Assertions.assertEquals("Tons    of   spaces Hereee!", token.evaluate(dummyContext));
    }

}
