package old.automated;

import net.programmer.igoodie.legacy.parser.TSLLexerOld;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

public class LexerTests {

    @Test
    public void shouldLexeSnippets() throws IOException {
        String script = TestUtils.loadTSLScript("snippets.tsl");

        TSLLexerOld lexer = new TSLLexerOld(script);
        lexer.lex();

        List<TSLSnippetBuffer> buffers = lexer.getSnippets();
        Assertions.assertEquals(buffers.get(0).getType(), TSLSnippetBuffer.Type.TAG);
        Assertions.assertEquals(buffers.get(1).getType(), TSLSnippetBuffer.Type.TAG);
        Assertions.assertEquals(buffers.get(2).getType(), TSLSnippetBuffer.Type.TAG);
        Assertions.assertEquals(buffers.get(3).getType(), TSLSnippetBuffer.Type.RULE);
        Assertions.assertEquals(buffers.get(4).getType(), TSLSnippetBuffer.Type.CAPTURE);
        Assertions.assertEquals(buffers.get(5).getType(), TSLSnippetBuffer.Type.RULE);
    }

}
