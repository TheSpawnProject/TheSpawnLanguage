package parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestStringUtils;

import java.util.ArrayList;
import java.util.List;

public class TokenTests {

    @Test
    @DisplayName("Group Tokens should preserve whitespace information")
    public void testGroupTokenEvaluationWithWhitespaces() {
        List<TSLToken> groupTokens = new ArrayList<>();
        groupTokens.add(new TSLPlainWord(0, 13, "HELLO"));
        groupTokens.add(new TSLPlainWord(1, 0, "WHAT"));
        groupTokens.add(new TSLPlainWord(1, 8, "IS"));
        groupTokens.add(new TSLExpression(2, 2, "\"YOUR\""));
        groupTokens.add(new TSLPlainWord(2, 12, "NAME?"));

        TSLGroup groupToken = new TSLGroup(0, 12, groupTokens);

        TSLContext tslContext = new TSLContext(new TheSpawnLanguage());

        Assertions.assertEquals(
                TestStringUtils.unescapeNewlines("%HELLO\nWHAT    IS\n${\"YOUR\"} NAME?%"),
                TestStringUtils.unescapeNewlines(groupToken.getRaw()));

        Assertions.assertEquals(
                TestStringUtils.unescapeNewlines("HELLO\nWHAT    IS\nYOUR NAME?"),
                TestStringUtils.unescapeNewlines(groupToken.evaluate(tslContext)));
    }

    @Test
    @DisplayName("Capture Parameter should disallow evaluation of itself")
    public void testCaptureParameterTokenEvaluation() {
        TSLCaptureParameter token = new TSLCaptureParameter(0, 0, "x");

        TSLContext context = new TSLContext(new TheSpawnLanguage());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
    }

    @Test
    @DisplayName("Capture Call should disallow evaluation of itself")
    public void testCaptureCallTokenEvaluation() {
        TSLCaptureCall token = new TSLCaptureCall(0, 0, "someCapture");

        TSLContext context = new TSLContext(new TheSpawnLanguage());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
    }

}
