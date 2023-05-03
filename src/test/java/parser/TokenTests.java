package parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.token.TSLExpression;
import net.programmer.igoodie.tsl.parser.token.TSLGroup;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TokenTests {

    @Test
    public void shouldPreserveGroupTokenWhitespaces() {
        List<TSLToken> groupTokens = new ArrayList<>();
        groupTokens.add(new TSLPlainWord(0, 13, "HELLO"));
        groupTokens.add(new TSLPlainWord(1, 0, "WHAT"));
        groupTokens.add(new TSLPlainWord(1, 8, "IS"));
        groupTokens.add(new TSLExpression(2, 2, "\"YOUR\""));
        groupTokens.add(new TSLPlainWord(2, 12, "NAME?"));

        TSLGroup groupToken = new TSLGroup(0, 12, groupTokens);

        TSLContext tslContext = new TSLContext(new TheSpawnLanguage());

        Assertions.assertEquals(
                unescapeNewlines("%HELLO\nWHAT    IS\n${\"YOUR\"} NAME?%"),
                unescapeNewlines(groupToken.getRaw()));

        Assertions.assertEquals(
                unescapeNewlines("HELLO\nWHAT    IS\nYOUR NAME?"),
                unescapeNewlines(groupToken.evaluate(tslContext)));
    }

    private String unescapeNewlines(String text) {
        return text.replaceAll("\\n", "\\\\n");
    }

}
