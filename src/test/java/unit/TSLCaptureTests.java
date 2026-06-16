package unit;

import example.action.PrintAction;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.*;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
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
                
                $c(x) = $b {{x}} (PRINT {{x}} %Hi There, {{x}} = | {{x}} |!%)
                
                $d = $c(${2+1}) 4
                """;

        TSLParser parser = TSLParser.fromScript(script);

        TSLPlatform platform = new TSLPlatform("Test Platform", 1.0f);
        platform.registerAction("PRINT", PrintAction::new);
        platform.pushExpressionEvaluator((ctx, expression) -> "3");

        TSLRuleset ruleset = parser.parseRuleset().resolve(platform);

        debugCapture(platform, ruleset, "a");
        debugCapture(platform, ruleset, "b");
        debugCapture(platform, ruleset, "c");
        debugCapture(platform, ruleset, "d");
    }

    private String debugCapture(TSLPlatform platform, TSLRuleset ruleset, String captureName) {
        TSLCapture capture = ruleset.getCapture(captureName).orElseThrow();

        TSLTemplateTransformer templateTransformer = new TSLTemplateTransformer(capture.getTemplate());
        templateTransformer.collapseCaptures(ruleset.getCaptures());
        List<TSLClause> resolvedClauses = templateTransformer.getClauses();

        TSLEventContext ctx = new TSLEventContext(platform, "Dummy Event");

        String sourceRebuilt = debugClause(resolvedClauses);

        System.out.println(sourceRebuilt);

        return sourceRebuilt;
    }

    private String debugClause(List<TSLClause> clauses) {
        return clauses.stream().map(clause -> {
            if (clause.isToken())
                return clause.asToken().getSource().stream().map(Token::getText).collect(Collectors.joining());
            if (clause.isNest()) return debugClause(clause.asNest().getClauses());
            return null;
        }).collect(Collectors.joining(" ", "(", ")"));
    }

}
