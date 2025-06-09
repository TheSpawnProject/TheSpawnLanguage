package unit;

import example.action.PrintAction;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TSLCaptureTests {

    @Test
    public void shouldResolveCaptureContent() {
        String script = """
                $a = 1
                
                $b = $a 2
                
                $c(x) = $b {{x}} (PRINT %Hi There, | {{x}} |!%)
                
                $d = $c(${2+1}) 4
                """;

        TSLParser parser = TSLParser.fromScript(script);

        TSLPlatform platform = new TSLPlatform("Test Platform", 1.0f);
        platform.registerAction("PRINT", PrintAction::new);
        platform.registerExpressionEvaluator(expression -> "3");

        TSLRuleset ruleset = parser.parseRuleset().resolve(platform);
        TSLCapture capture = ruleset.getCapture("d").orElseThrow();

        List<Either<TSLWord, TSLAction>> resolvedContent =
                capture.resolveContent(ruleset.getCaptures(), Collections.emptyList());

        TSLEventContext ctx = new TSLEventContext(platform, "Dummy Event");

        String sourceRebuilt = resolvedContent.stream().map(content -> content.reduce(
                tslWord -> tslWord.evaluate(ctx),
                tslAction -> tslAction.getSourceArguments().stream().map(actionContent -> actionContent.reduce(
                        tslWord2 -> tslWord2.evaluate(ctx),
                        tslAction2 -> tslAction2.toString()
                )).collect(Collectors.joining(" ", "(", ")"))
        )).collect(Collectors.joining(" "));

        System.out.println(sourceRebuilt);
    }

}
