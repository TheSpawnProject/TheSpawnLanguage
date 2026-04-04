package unit;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class YieldModificationTest {

    private static class TestSumAction extends TSLAction {

        protected double sum = 0;

        public TestSumAction(List<TSLClause> sourceArguments) throws TSLSyntaxException {
            super(sourceArguments);
        }

        @Override
        public void parseArguments_OLD(TSLPlatform platform) throws TSLSyntaxException {
            for (TSLClause sourceArgument : this.sourceArguments) {
                TSLToken word = sourceArgument.expectToken();
                if (!(word instanceof TSLPlainWord plainWord)) {
                    throw new TSLSyntaxException("Expected a plain word").atWord(word);
                }
                try {
                    String value = plainWord.getValue();
                    double doubleValue = Double.parseDouble(value);
                    this.sum += doubleValue;
                } catch (NumberFormatException e) {
                    throw new TSLSyntaxException("Expected a number format").atWord(word);
                }
            }
        }

        @Override
        public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
            TSLPlainWord result = new TSLPlainWord(String.valueOf(this.sum));
            return Collections.singletonList(result);
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
                  YIELDING $result
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
        TSLToken firstWord = capture.getTemplate().get(0).getToken().orElseThrow();
        System.out.println(firstWord.evaluate(ctx));
    }

}
