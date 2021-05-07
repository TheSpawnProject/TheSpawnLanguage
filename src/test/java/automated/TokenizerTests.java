package automated;

import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}
