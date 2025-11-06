package unit;

import example.action.PrintAction;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLCaptureResolver;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.Token;
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
        platform.pushExpressionEvaluator(expression -> "3");

        TSLRuleset ruleset = parser.parseRuleset().resolve(platform);
        TSLCapture capture = ruleset.getCapture("d").orElseThrow();

        TSLCaptureResolver captureResolver = new TSLCaptureResolver(ruleset.getCaptures(), capture, Collections.emptyList());
        List<Either<TSLWord, TSLAction>> resolvedClause = captureResolver.resolve();

        TSLEventContext ctx = new TSLEventContext(platform, "Dummy Event");

        String sourceRebuilt = debugClause(resolvedClause);

        System.out.println(sourceRebuilt);
    }

    private String debugClause(List<Either<TSLWord, TSLAction>> clause) {
        return clause.stream()
                .map(content -> content.reduce(
                        word -> word.getSource().stream().map(Token::getText).collect(Collectors.joining()),
                        nest -> debugClause(nest.getSourceArguments())
                ))
                .collect(Collectors.joining(" ", "(", ")"));
    }

}
