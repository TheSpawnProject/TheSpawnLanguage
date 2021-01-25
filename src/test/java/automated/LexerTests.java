package automated;

import com.google.common.io.Resources;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLSnippet;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class LexerTests {

    @Test
    public void foo() throws IOException {
        List<String> lines = Resources.readLines(Resources.getResource("tsl/sample.tsl"), StandardCharsets.UTF_8);
        String script = String.join("\n", lines);

        System.out.println(script);
        System.out.println("/--------------------------------/");

        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        for (TSLSnippet snippet : lexer.getSnippets()) {
            System.out.println(snippet);
        }
    }

}
