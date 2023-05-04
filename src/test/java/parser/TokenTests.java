package parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.ArrayList;
import java.util.List;

public class TokenTests {

    @Test
    @DisplayName("Group Tokens should preserve whitespace information")
    public void testGroupTokenEvaluationWithWhitespaces() {
        List<TSLToken> groupedTokens = new ArrayList<>();
        groupedTokens.add(new TSLPlainWord(
                new TextPosition(0, 15),
                new TextPosition(0, 15 + "HELLO".length() - 1),
                "HELLO"));
        groupedTokens.add(new TSLPlainWord(
                new TextPosition(1, 0),
                new TextPosition(1, "WHAT".length() - 1),
                "WHAT"));
        groupedTokens.add(new TSLPlainWord(
                new TextPosition(1, 8),
                new TextPosition(1, 8 + "IS".length() - 1),
                "IS"));
        groupedTokens.add(new TSLExpression(
                new TextPosition(2, 2),
                new TextPosition(2, 2 + "${\"YOUR\"}".length() - 1),
                "\"YOUR\""));
        groupedTokens.add(new TSLPlainWord(
                new TextPosition(2, 12),
                new TextPosition(2, 12 + "NAME?".length() - 1),
                "NAME?"));

        TSLGroup groupToken = new TSLGroup(
                new TextPosition(0, 12),
                new TextPosition(2, 27),
                groupedTokens);

        TSLContext tslContext = new TSLContext(new TheSpawnLanguage());

        Assertions.assertEquals(
                TestUtils.unescapeNewlines("%  HELLO\nWHAT    IS\n${\"YOUR\"} NAME?          %"),
                TestUtils.unescapeNewlines(groupToken.getRaw()));

        Assertions.assertEquals(
                TestUtils.unescapeNewlines("  HELLO\nWHAT    IS\nYOUR NAME?          "),
                TestUtils.unescapeNewlines(groupToken.evaluate(tslContext)));
    }

    @Test
    @DisplayName("Empty Group Tokens should preserve whitespace information")
    public void testEmptyGroupTokenEvaluationWithWhitespaces() {
        List<TSLToken> groupTokens = new ArrayList<>();

        TSLGroup groupToken = new TSLGroup(
                new TextPosition(0, 5),
                new TextPosition(2, 7),
                groupTokens);

        TSLContext tslContext = new TSLContext(new TheSpawnLanguage());

        Assertions.assertEquals(
                TestUtils.unescapeNewlines("%\n\n %"),
                TestUtils.unescapeNewlines(groupToken.getRaw()));

        Assertions.assertEquals(
                TestUtils.unescapeNewlines("\n\n "),
                TestUtils.unescapeNewlines(groupToken.evaluate(tslContext)));
    }

    @Test
    @DisplayName("Capture Parameter token should disallow evaluation of itself")
    public void testCaptureParameterTokenEvaluation() {
        TSLCaptureParameter token = new TSLCaptureParameter(
                new TextPosition(0, 0),
                new TextPosition(0, "{{x}}".length()),
                "x");

        TSLContext context = new TSLContext(new TheSpawnLanguage());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
    }

    @Test
    @DisplayName("Capture Call token should disallow evaluation of itself")
    public void testCaptureCallTokenEvaluation() {
        TSLCaptureCall token = new TSLCaptureCall(
                new TextPosition(0, 0),
                new TextPosition(0, "$someCapture".length()),
                "someCapture");

        TSLContext context = new TSLContext(new TheSpawnLanguage());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
    }

    @Test
    @DisplayName("Decorator Call token should disallow evaluation of itself")
    public void testDecoratorCallTokenEvaluation() {
        TSLDecoratorCall token = new TSLDecoratorCall(
                new TextPosition(0, 0),
                new TextPosition(0, "@someDecorator".length()),
                "someDecorator");

        TSLContext context = new TSLContext(new TheSpawnLanguage());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
    }

}
