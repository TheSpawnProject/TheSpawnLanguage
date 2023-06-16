package parser;

import net.programmer.igoodie.tsl.parser.helper.Either;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import net.programmer.igoodie.tsl.parser.snippet.base.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LexerTests {

    @Test
    public void testSimpleSnippet() {
        String script = "FOO BAR BAZ";
        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        List<TSLUnparsedSnippet> snippets = lexer.getSnippets();

        Assertions.assertEquals(1, snippets.size());
        Assertions.assertEquals(new TSLUnparsedSnippet(
                Stream.of(
                                new TSLPlainWord(new TextRange(0, 0, 0, 2), "FOO"),
                                new TSLPlainWord(new TextRange(0, 4, 0, 6), "BAR"),
                                new TSLPlainWord(new TextRange(0, 8, 0, 10), "BAZ")
                        )
                        .map(Either::<TSLToken, TSLSnippet<?>>left)
                        .collect(Collectors.toList())
        ), snippets.get(0));
    }

}
