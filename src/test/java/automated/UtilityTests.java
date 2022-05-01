package automated;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLExpression;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UtilityTests {

    @Test
    public void collectionsFlatterTest() {
        List<Object> elements = Arrays.asList(
                new TSLPlainWord(0, 0, "Hello"),
                new TSLPlainWord(0, 0, "World!"),
                new TSLToken(0, 0) {
                    @Override
                    public String getTypeName() {
                        return null;
                    }

                    @Override
                    public String getRaw() {
                        return null;
                    }

                    @Override
                    public String evaluate(TSLContext context) {
                        return null;
                    }
                },
                new TSLSnippet(null, Arrays.asList(
                        new TSLPlainWord(0, 0, "HelloSnippet"),
                        new TSLPlainWord(0, 0, "WorldSnippet!"))) {},
                Arrays.asList(
                        new TSLExpression(0, 0, "Hello World!"),
                        new TSLExpression(0, 0, "Hello World2")
                ));

        List<TSLToken> flattened = CollectionUtils.flatAll(TSLToken.class, elements);

        System.out.println(elements);
        System.out.println(flattened);

        Assertions.assertEquals(7, flattened.size());
    }

}
