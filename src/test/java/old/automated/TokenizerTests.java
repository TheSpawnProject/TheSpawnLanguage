package old.automated;

import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;

public class TokenizerTests {

    @Test
    public void shouldTokenizeTypes() {
        TSLTokenizer tokenizer = new TSLTokenizer();

        Assertions.assertEquals(tokenizer.tokenize("String").getClass(), TSLString.class);
        Assertions.assertEquals(tokenizer.tokenize("=").getClass(), TSLSymbol.class);
        Assertions.assertEquals(tokenizer.tokenize("(foo (bar))").getClass(), TSLNest.class);
        Assertions.assertEquals(tokenizer.tokenize("$call").getClass(), TSLCaptureCall.class);
        Assertions.assertEquals(tokenizer.tokenize("${_max(1,2)}").getClass(), TSLExpression.class);
        Assertions.assertEquals(tokenizer.tokenize("%A group!%").getClass(), TSLGroup.class);
    }

    @Test
    public void shouldTokenizeNestDepths() throws IOException {
        String nestRaw = TestUtils.loadFragment("nest_1");
        TSLNest nest = (TSLNest) new TSLTokenizer().tokenize(nestRaw);

        Assertions.assertEquals(6, nest.depth());
    }

}
