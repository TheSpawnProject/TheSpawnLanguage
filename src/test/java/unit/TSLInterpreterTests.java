package unit;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.interpreter.TSLActionInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLCaptureInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLRuleInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLRulesetInterpreter;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.TSLDeferred;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.definition.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class TSLInterpreterTests {

    private static class DemoDropAction extends TSLAction {
        protected TSLWord droppedItemId;

        public DemoDropAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException {
            super(platform, args);
            this.droppedItemId = args.get(0).getLeft().orElseThrow();
        }

        @Override
        public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
            System.out.println("Dropping items " + droppedItemId.evaluate(ctx));
            return Collections.singletonList(droppedItemId);
        }
    }

    private static class DemoSummonAction extends TSLAction {
        protected TSLWord mobId;

        public DemoSummonAction(TSLPlatform platform, List<Either<TSLWord, TSLAction>> args) throws TSLSyntaxException {
            super(platform, args);
            this.mobId = args.get(0).getLeft().orElseThrow();
        }

        @Override
        public List<TSLWord> perform(TSLEventContext ctx) throws TSLPerformingException {
            System.out.println("Summoning mob " + mobId.evaluate(ctx));
            return Collections.singletonList(mobId);
        }
    }

    private static TSLPlatform getDemoPlatform() {
        TSLPlatform platform = new TSLPlatform("Test Platform", 1.0f);

        platform.initializeStd();

        // Register Actions
        platform.registerAction("DROP", DemoDropAction::new);
        platform.registerAction("SUMMON", DemoSummonAction::new);

        // Register Events
        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(TSLEvent.Property.Builder.INT.create("amount")));

        // Register Expr Evaluators
        platform.registerExpressionEvaluator(expression -> "true");

        return platform;
    }

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

        TSLPlatform demoPlatform = getDemoPlatform();

        List<Either<TSLWord, TSLAction>> contents1 = deferredCapture1.resolve(demoPlatform).getContents();
        List<Either<TSLWord, TSLAction>> contents2 = deferredCapture2.resolve(demoPlatform).getContents();

        System.out.println(contents1);
        System.out.println(contents2);
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

        TSLPlatform platform = getDemoPlatform();

        TSLRule rule = deferredRule.resolve(platform);

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ctx.getEventArgs().put("amount", 1200);
        List<TSLWord> yield = rule.perform(ctx);

        System.out.println(rule.getAction().getDisplaying());

        System.out.println("Yield " + yield);
    }

    @Test
    public void shouldInterpretRuleset() {
        String script = """
                DROP diamond ON Donation WITH amount = 1
                
                $x = 5
                
                DROP apple ON Donation WITH amount = 2
                
                $y = FOO BAR
                
                DROP diamond ON Donation WITH amount = 2
                
                $z = (DROP diamond)
                
                #**       
                 * This is my
                 * <strong>TSLDoc hurraay!</strong>
                 *
                 *
                 *#
                #DO $z ON Donation WITH amount = 4
                DROP diamond ON Donation WITH amount = 4
                """;

        TSLLexer lexer = new TSLLexer(CharStreams.fromString(script));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TSLParserImpl parserImpl = new TSLParserImpl(tokenStream);

        TSLParserImpl.TslRulesetContext rulesetTree = parserImpl.tslRuleset();

        TSLDeferred<TSLRuleset> deferredRuleset = new TSLRulesetInterpreter().interpret(rulesetTree);

        System.out.println(deferredRuleset);

        TSLPlatform platform = getDemoPlatform();

        TSLRuleset ruleset = deferredRuleset.resolve(platform);

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ctx.getEventArgs().put("amount", 2);
        List<TSLWord> yield = ruleset.perform(ctx);

        System.out.println("Yield " + yield);
    }

}
