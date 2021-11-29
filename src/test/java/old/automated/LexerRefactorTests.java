package old.automated;

import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;

public class LexerRefactorTests {

    @Test
    public void foo() throws IOException {
        String script = TestUtils.loadTSLScript("sample2.tsl");

        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        System.out.println("Snippet count = " + lexer.getSnippets().size());

        for (TSLSnippetBuffer snippet : lexer.getSnippets()) {
            System.out.println(snippet);
        }
    }

}
