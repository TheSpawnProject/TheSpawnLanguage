package unit;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.interpreter.TSLActionInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLCaptureInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLRuleInterpreter;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class TSLInterpreterTests {

    @Test
    public void shouldInterpretAction() {
        String script = """
                DO (DROP diamond
                    YIELDS $foo
                    DISPLAYING %Diamonds!%)
                    DISPLAYING %Diamonds?%
                ON Donation
                """;

        TSLActionInterpreter interpreter = new TSLActionInterpreter();

        TSLLexer lexer = new TSLLexer(CharStreams.fromString(script));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TSLParserImpl parserImpl = new TSLParserImpl(tokenStream);

        TSLParserImpl.TslRulesContext ast = parserImpl.tslRules();
        List<TSLParserImpl.TslRuleContext> rulesAst = ast.tslRule();

        TSLDeferred<TSLAction> deferredAction = interpreter.interpret(rulesAst.get(0).reactionRule().action());

        System.out.println(deferredAction);
    }

    @Test
    public void shouldInterpretCapture() {
        String script = """
                 $summonMob(mobId, name) = (
                    SUMMON {{mobId}}
                    DISPLAYING %[{text:"| {{name}} | spawned!", color:"blue"}]%
                 )
                
                 $summonMobWithItem(mobId, name, itemId) = (
                     SUMMON {{mobId}} ~ ~ ~ %{HandItems:[{id:"| {{itemId}} |",Count:1b},{}]}%
                     DISPLAYING %[{text:"| {{name}} | spawned!", color:"blue"}]%
                 )
                """;

        TSLCaptureInterpreter interpreter = new TSLCaptureInterpreter();

        TSLLexer lexer = new TSLLexer(CharStreams.fromString(script));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TSLParserImpl parserImpl = new TSLParserImpl(tokenStream);

        TSLParserImpl.TslRulesContext ast = parserImpl.tslRules();
        List<TSLParserImpl.TslRuleContext> rulesAst = ast.tslRule();

        TSLDeferred<TSLCapture> deferredCapture1 = interpreter.interpret(rulesAst.get(0).captureRule());
        TSLDeferred<TSLCapture> deferredCapture2 = interpreter.interpret(rulesAst.get(1).captureRule());

        System.out.println(deferredCapture1);
        System.out.println(deferredCapture2);
    }

    @Test
    public void shouldInterpretRule() {
        String script = """
                DO (DROP diamond
                    YIELDS $foo
                    DISPLAYING %Diamonds!%)
                    DISPLAYING %Diamonds?%
                ON Donation
                WITH ${event.amount >= 999}
                WITH amount >= 999
                WITH amount <= 1999
                WITH amount = 1200
                """;

        TSLLexer lexer = new TSLLexer(CharStreams.fromString(script));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TSLParserImpl parserImpl = new TSLParserImpl(tokenStream);

        TSLParserImpl.TslRulesContext ast = parserImpl.tslRules();
        TSLParserImpl.ReactionRuleContext ruleTree = ast.tslRule().get(0).reactionRule();

        TSLDeferred<TSLRule> deferredRule = new TSLRuleInterpreter().interpret(ruleTree);

        System.out.println(deferredRule);

        TSLPlatform platform = new TSLPlatform("Test Platform", 1.0f);

        platform.initializeStd();
        platform.registerAction("DROP", (_platform, args) -> new TSLAction(_platform, args) {
            @Override
            public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
                TSLWord droppedItemId = args.get(0).getLeft().orElseThrow();
                System.out.println("Dropping items " + droppedItemId.evaluate(ctx));
                return Collections.singletonList(droppedItemId);
            }
        });
        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(TSLEvent.Property.Builder.INT.create("amount")));
        platform.registerExpressionEvaluator(expression -> "true");

        TSLRule rule = deferredRule.resolve(platform).orElseThrow();

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ctx.getEventArgs().put("amount", 1200);
        List<TSLWord> yield = rule.perform(ctx);

        System.out.println("Yield " + yield);
    }

    @Test
    public void shouldInterpretRuleset() {

    }

}
