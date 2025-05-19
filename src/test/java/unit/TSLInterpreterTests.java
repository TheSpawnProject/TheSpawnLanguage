package unit;

import net.programmer.igoodie.tsl.interpreter.TSLActionInterpreter;
import net.programmer.igoodie.tsl.interpreter.TSLCaptureInterpreter;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TSLInterpreterTests {

    @Test
    public void shouldInterpretActionRef() {
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

        TSLAction.Ref actionRef = interpreter.interpret(rulesAst.get(0).reactionRule().action());

        System.out.println(actionRef);
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

        TSLCapture.Ref capture1 = interpreter.interpret(rulesAst.get(0).captureRule());
        TSLCapture.Ref capture2 = interpreter.interpret(rulesAst.get(1).captureRule());

        System.out.println(capture1);
        System.out.println(capture2);
    }

}
