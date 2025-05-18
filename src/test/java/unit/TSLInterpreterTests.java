package unit;

import net.programmer.igoodie.tsl.interpreter.TSLCaptureInterpreter;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParserImpl;
import net.programmer.igoodie.tsl.runtime.TSLCapture;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TSLInterpreterTests {

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

        TSLCapture capture1 = interpreter.interpret(rulesAst.get(0).captureRule());
        TSLCapture capture2 = interpreter.interpret(rulesAst.get(1).captureRule());

        System.out.println(capture1);
        System.out.println(capture2);
    }

}
