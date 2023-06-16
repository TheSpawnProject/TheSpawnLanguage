package parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TokenTests {

    public static class TSLGroupTests {

        @Test
        @DisplayName("Group Tokens should preserve whitespace information")
        public void testGroupTokenEvaluationWithWhitespaces() {
            TSLGroup groupToken = new TSLGroup(
                    new TextRange(0, 12, 2, 29),
                    "  HELLO\nWHAT    IS\n|${\"YOUR\"}| NAME?          ",
                    new TSLGroup.ExpressionListBuilder(ArrayList::new)
                            .addElement(new TSLGroup.ExpressionToken(
                                    new TextRange(2, 2, 2, 12),
                                    19, 29,
                                    new TSLExpression(
                                            new TextRange(2, 3, 2, 11),
                                            "\"YOUR\""
                                    )
                            ))
                            .build());

            TSLContext tslContext = new TSLContext(new TheSpawnLanguage());

            Assertions.assertEquals(
                    TestUtils.unescapeNewlines("%  HELLO\nWHAT    IS\n|${\"YOUR\"}| NAME?          %"),
                    TestUtils.unescapeNewlines(groupToken.getRaw()));

            Assertions.assertEquals(
                    TestUtils.unescapeNewlines("  HELLO\nWHAT    IS\nYOUR NAME?          "),
                    TestUtils.unescapeNewlines(groupToken.evaluate(tslContext)));
        }

        @Test
        @DisplayName("Empty Group Tokens should preserve whitespace information")
        public void testEmptyGroupTokenEvaluationWithWhitespaces() {
            TSLGroup groupToken = new TSLGroup(
                    new TextRange(0, 5, 2, 7),
                    "\n\n ",
                    Collections.emptyList());

            TSLContext tslContext = new TSLContext(new TheSpawnLanguage());

            Assertions.assertEquals(
                    TestUtils.unescapeNewlines("%\n\n %"),
                    TestUtils.unescapeNewlines(groupToken.getRaw()));

            Assertions.assertEquals(
                    TestUtils.unescapeNewlines("\n\n "),
                    TestUtils.unescapeNewlines(groupToken.evaluate(tslContext)));
        }

        @Test
        @DisplayName("Group Tokens should be able to fill capture parameters correctly")
        public void testCaptureParameterFilling() {
            TSLGroup groupToken = new TSLGroup(
                    new TextRange(0, 0, 0, 23),
                    "Hey there, |{{name}}|!",
                    new TSLGroup.ExpressionListBuilder(ArrayList::new)
                            .addElement(new TSLGroup.ExpressionToken(
                                    new TextRange(0, 12, 0, 21),
                                    11, 20,
                                    new TSLCaptureParameter(
                                            new TextRange(0, 13, 0, 20),
                                            "name"
                                    )
                            ))
                            .build());

            Map<String, TSLToken> arguments = new HashMap<>();
            arguments.put("name", new TSLPlainWord(
                    new TextRange(10, 10, 10, 10 + "iGoodiexx".length() + 1),
                    "iGoodiexx"
            ));

            Assertions.assertEquals(
                    TestUtils.unescapeNewlines("%Hey there, |{{name}}|!%"),
                    TestUtils.unescapeNewlines(groupToken.getRaw()));

            Assertions.assertEquals(
                    TestUtils.unescapeNewlines("%Hey there, |iGoodiexx|!%"),
                    TestUtils.unescapeNewlines(groupToken.fillCaptureParameters(arguments).getRaw()));
        }

    }

    public static class TSLCaptureParameterTests {

        @Test
        @DisplayName("Capture Parameter token should disallow evaluation of itself")
        public void testCaptureParameterTokenEvaluation() {
            TSLCaptureParameter token = new TSLCaptureParameter(
                    new TextRange(0, 0, 0, "{{x}}".length()),
                    "x");

            TSLContext context = new TSLContext(new TheSpawnLanguage());
            Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
        }

    }

    public static class TSLCaptureCallTests {

        @Test
        @DisplayName("Capture Call token should disallow evaluation of itself")
        public void testCaptureCallTokenEvaluation() {
            TSLCaptureCall token = new TSLCaptureCall(
                    new TextRange(0, 0, 0, "$$someCapture".length()),
                    "someCapture");

            TSLContext context = new TSLContext(new TheSpawnLanguage());
            Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
        }

    }

    public static class TSLDecoratorCallTests {

        @Test
        @DisplayName("Decorator Call token should disallow evaluation of itself")
        public void testDecoratorCallTokenEvaluation() {
            TSLDecoratorCall token = new TSLDecoratorCall(
                    new TextRange(0, 0, 0, "@someDecorator".length()),
                    "someDecorator");

            TSLContext context = new TSLContext(new TheSpawnLanguage());
            Assertions.assertThrows(UnsupportedOperationException.class, () -> token.evaluate(context));
        }

    }

}
