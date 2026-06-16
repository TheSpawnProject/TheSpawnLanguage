package unit;

import example.action.PrintAction;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.TSLTemplateTransformer;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLGroup;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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

    @Test
    public void shouldCollapsePlaceholders() {
        String script = """
                $func(x, y, z) =
                    {{x}}
                    %Group | {{x}} |%
                    ${"Expression" + {{x}}}
                    (NESTED
                        {{x}}
                        %Nested Group | {{x}} |%
                        ${"Nested Expression" + {{x}}}
                        YIELDING $result
                        DISPLAYING %Resulting Message%)
                """;

        TSLCapture capture = TSLParser.fromScript(script).parseCapture();
        List<TSLClause> argumentTokens = TSLParser.fromScript("ARG0 ARG1 ARG2").parseTokens()
                .stream().map(tslToken -> ((TSLClause) tslToken)).toList();

        Map<String, TSLClause> argumentMap = TSLTemplateTransformer.composeArgumentMap(capture.getParamNames(), argumentTokens);
        System.out.println(argumentMap);

        TSLTemplateTransformer transformer = new TSLTemplateTransformer(capture.getTemplate());
        transformer.collapsePlaceholders2(argumentMap);
        debugClause(transformer.getClauses());
    }

    @Test
    public void shouldCollapsePlaceholdersInGroup() {
        String script = "%Group | {{x}} | should | % | {{x}} | % | be replaced.%";

        TSLGroup group = TSLParser.fromScript(script).parseTokens().get(0).expectToken(TSLGroup.class);
        List<TSLClause> argumentTokens = TSLParser.fromScript("ARG0").parseTokens()
                .stream().map(tslToken -> ((TSLClause) tslToken)).toList();

        Map<String, TSLClause> argumentMap = TSLTemplateTransformer.composeArgumentMap(List.of("x"), argumentTokens);
        System.out.println(argumentMap);

        TSLTemplateTransformer transformer = new TSLTemplateTransformer(List.of(group));
        transformer.collapsePlaceholders2(argumentMap);
        debugClause(transformer.getClauses());
    }

    /* -------------------------- */

    private void debugCapture(TSLPlatform platform, TSLRuleset ruleset, String captureName) {
        TSLCapture capture = ruleset.getCapture(captureName).orElseThrow();

        TSLTemplateTransformer templateTransformer = new TSLTemplateTransformer(capture.getTemplate());
        templateTransformer.collapseCaptures(ruleset.getCaptures());
        List<TSLClause> resolvedClauses = templateTransformer.getClauses();

        TSLEventContext ctx = new TSLEventContext(platform, "Dummy Event");

        debugClause(resolvedClauses);
    }

    private void debugClause(List<TSLClause> clauses) {
        for (int i = 0; i < clauses.size(); i++) {
            TSLClause clause = clauses.get(i);
            System.out.println("#" + (i + 1) + " - " +
                    clause.toDebugString());
        }
    }
}
