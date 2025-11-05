package unit;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.junit.jupiter.api.Test;

import java.util.List;

public class YieldModificationTest {

    private static class TestSumAction extends TSLAction {

        public TestSumAction(List<Either<TSLWord, TSLAction>> sourceArguments) throws TSLSyntaxException {
            super(sourceArguments);
        }

        @Override
        public void interpretArguments(TSLPlatform platform) throws TSLSyntaxException {

        }

        @Override
        public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
            // TODO: Sum given numbers
            TSLPlainWord result = new TSLPlainWord("2");
            return List.of(result);
        }

    }

    private static TSLPlatform getDemoPlatform() {
        TSLPlatform platform = new TSLPlatform("Test Platform", 1.0f);

        platform.initializeStd();

        // Register Actions
        platform.registerAction("SUM", TestSumAction::new);

        // Register Events
        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(TSLEvent.Property.Builder.INT.create("amount")));

        // Bind Expr Evaluator
        platform.pushExpressionEvaluator(expression -> "true");

        return platform;
    }

    @Test
    public void testCase() {
        String script = """
                $result = 0
                
                SUM $result 1
                  YIELDS $result
                  ON Donation
                """;

        TSLPlatform platform = getDemoPlatform();

        TSLParser parser = TSLParser.fromScript(script);

        TSLRuleset ruleset = parser.parseRuleset().resolve(platform);

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");

        ruleset.perform(ctx);
        ruleset.perform(ctx);
        ruleset.perform(ctx);

        TSLCapture capture = ruleset.getCapture("result").orElseThrow();
        TSLWord firstWord = capture.getContents().get(0).getLeft().orElseThrow();
        System.out.println(firstWord.evaluate(ctx));
    }

}
