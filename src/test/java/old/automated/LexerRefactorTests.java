package old.automated;

import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
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

        for (TSLTokenBuffer snippet : lexer.getSnippets()) {
            System.out.println(snippet);
        }
    }

}
