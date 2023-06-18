package parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.TSLUnparsedSnippet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TSLAssertions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LexerTests {

    @Test
    @DisplayName("Lexer should be able to nest snippets")
    public void testNestedSnippets() {
        String script = "FOO BAR BAZ (HEY (THERE) MATE!)";
        TSLLexer lexer = new TSLLexer(script);
        List<TSLUnparsedSnippet> snippets = lexer.lex();

        Assertions.assertEquals(1, snippets.size());
        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLPlainWord(FOO) @ (L0:0 | L0:2)",
                "1- TSLPlainWord(BAR) @ (L0:4 | L0:6)",
                "1- TSLPlainWord(BAZ) @ (L0:8 | L0:10)",
                " 2- TSLPlainWord(HEY) @ (L0:13 | L0:15)",
                "  3- TSLPlainWord(THERE) @ (L0:18 | L0:22)",
                " 2- TSLPlainWord(MATE!) @ (L0:25 | L0:29)"
        ), snippets.get(0));
    }

    @Test
    @DisplayName("Lexer should be able to skip comments")
    public void testComments() {
        String script = "#! Foo Bar Baz #Comment!\n\nB B#**\nComment\nComment Comment*#\n\nC#*\n Comment\n Foo bar\n*#\n\nD #* A looong comment *#\n\n#Hmmm\nE\n\nF (C#*\n   Comment\n   Foo bar\n  *#)\n\nFoo\n#Hmmm\nBar #Comment\nBaz";

        TSLLexer lexer = new TSLLexer(script);
        List<TSLUnparsedSnippet> snippets = lexer.lex();

        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLSymbol(#!) @ (L0:0 | L0:1)",
                "1- TSLPlainWord(Foo) @ (L0:3 | L0:5)",
                "1- TSLPlainWord(Bar) @ (L0:7 | L0:9)",
                "1- TSLPlainWord(Baz) @ (L0:11 | L0:13)"
        ), snippets.get(0));

        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLPlainWord(B) @ (L2:0 | L2:0)",
                "1- TSLPlainWord(B) @ (L2:2 | L2:2)",
                " 2- TSLSymbol(#**) @ (L2:3 | L2:5)",
                " 2- TSLPlainWord(Comment) @ (L3:0 | L3:6)",
                " 2- TSLPlainWord(Comment) @ (L4:0 | L4:6)",
                " 2- TSLPlainWord(Comment) @ (L4:8 | L4:14)",
                " 2- TSLSymbol(*#) @ (L4:15 | L4:16)"
        ), snippets.get(1));

        TSLAssertions.assertSnippetsEqual(Collections.singletonList(
                "1- TSLPlainWord(C) @ (L6:0 | L6:0)"
        ), snippets.get(2));

        TSLAssertions.assertSnippetsEqual(Collections.singletonList(
                "1- TSLPlainWord(D) @ (L11:0 | L11:0)"
        ), snippets.get(3));

        TSLAssertions.assertSnippetsEqual(Collections.singletonList(
                "1- TSLPlainWord(E) @ (L14:0 | L14:0)"
        ), snippets.get(4));

        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLPlainWord(F) @ (L16:0 | L16:0)",
                " 2- TSLPlainWord(C) @ (L16:3 | L16:3)"
        ), snippets.get(5));

        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLPlainWord(Foo) @ (L21:0 | L21:2)",
                "1- TSLPlainWord(Bar) @ (L23:0 | L23:2)",
                "1- TSLPlainWord(Baz) @ (L24:0 | L24:2)"
        ), snippets.get(6));
    }

    @Test
    @DisplayName("Lexer should be able to read complex expressions")
    public void testExpressions() {
        //${{ \ key: `Hi there \${ ${true ? "{Jack`o Lantern}!" : "'\"'Friend\'s'\"'"}. How are ya?`}["key"]}
        //  ^        ^             ^        ^                 ^   ^                 ^^             ^^ ^   ^ ^
        //0 1        2             3        4                 3   4                 32             10 1   0 *

        String script = "${{ \\ key: `Hi there \\${ ${true ? \"{Jack`o Lantern}!\" : \"'\\\"'Friend\\'s'\\\"'\"}. How are ya?`}[\"key\"]}";

        TSLLexer lexer = new TSLLexer(script);
        List<TSLUnparsedSnippet> snippets = lexer.lex();

        TSLAssertions.assertSnippetsEqual(Collections.singletonList(
                "1- TSLExpression(${{ \\ key: `Hi there \\${ ${true ? \"{Jack`o Lantern}!\" : \"'\\\"'Friend\\'s'\\\"'\"}. How are ya?`}[\"key\"]}) @ (L0:0 | L0:98)"
        ), snippets.get(0));
    }

    @Test
    @DisplayName("Lexer should be able to escape at root level")
    public void testRootEscape() {
        String script;
        List<TSLUnparsedSnippet> snippets;

        script = "\\${plainText}";
        snippets = new TSLLexer(script).lex();

        TSLAssertions.assertSnippetsEqual(Collections.singletonList(
                "1- TSLPlainWord(${plainText}) @ (L0:1 | L0:12)"
        ), snippets.get(0));

        // ------------------------

        Assertions.assertThrows(TSLSyntaxError.class,
                () -> new TSLLexer("\\a\nb").lex());

        // ------------------------

        script = "Foo = \\#! #! =Unknown";
        snippets = new TSLLexer(script).lex();

        TSLAssertions.assertSnippetsEqual(Arrays.asList(
                "1- TSLPlainWord(Foo) @ (L0:0 | L0:2)",
                "1- TSLSymbol(=) @ (L0:4 | L0:4)",
                "1- TSLPlainWord(#!) @ (L0:7 | L0:8)",
                "1- TSLSymbol(#!) @ (L0:10 | L0:11)",
                "1- TSLPlainWord(=Unknown) @ (L0:13 | L0:20)"
        ), snippets.get(0));
    }

}
