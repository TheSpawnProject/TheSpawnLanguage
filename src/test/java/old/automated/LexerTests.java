package old.automated;

import net.programmer.igoodie.legacy.parser.TSLLexerOld;
import net.programmer.igoodie.tsl.parser.TSLTokenBuffer;
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

        List<TSLTokenBuffer> buffers = lexer.getSnippets();
        Assertions.assertEquals(buffers.get(0).getType(), TSLTokenBuffer.Type.TAG);
        Assertions.assertEquals(buffers.get(1).getType(), TSLTokenBuffer.Type.TAG);
        Assertions.assertEquals(buffers.get(2).getType(), TSLTokenBuffer.Type.TAG);
        Assertions.assertEquals(buffers.get(3).getType(), TSLTokenBuffer.Type.RULE);
        Assertions.assertEquals(buffers.get(4).getType(), TSLTokenBuffer.Type.CAPTURE);
        Assertions.assertEquals(buffers.get(5).getType(), TSLTokenBuffer.Type.RULE);
    }

}
