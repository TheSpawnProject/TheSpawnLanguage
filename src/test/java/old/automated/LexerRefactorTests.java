package old.automated;

import net.programmer.igoodie.legacy.parser.TSLLexerOld;
import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;

public class LexerRefactorTests {

    @Test
    public void foo() throws IOException {
        String script = TestUtils.loadTSLScript("sample2.tsl");

        TSLLexerOld lexer = new TSLLexerOld(script);
        lexer.lex();

        System.out.println("Snippet count = " + lexer.getSnippets().size());

        for (TSLTokenBuffer snippet : lexer.getSnippets()) {
            System.out.println(snippet);
        }
    }

}
