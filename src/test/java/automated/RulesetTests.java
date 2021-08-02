package automated;

import example.setup.ExamplePlugin;
import example.setup.event.AlertEvent;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RulesetTests {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
        TSL.loadPlugin(new ExamplePlugin());
    }

    @Test
    public void foo() throws IOException {
        String script = TestFiles.loadTSLScript("sample2.tsl");

        TSLLexer lexer = new TSLLexer(script).lex();

        for (TSLSnippetBuffer snippet : lexer.getSnippets()) {
            System.out.println(snippet);
        }

        System.out.println("\n--------------------\n");

        TSLParser parser = new TSLParser(TSL);
        TSLRuleset ruleset = parser.parse(script);

        System.out.println("Rule count: " + ruleset.getRules().size());

        TSLContext context = new TSLContext();
        context.setEvent(AlertEvent.INSTANCE);
        context.setEngine(TSL.getJsEngine());
        context.setEventArguments(new GoodieObject());

        for (List<TSLToken> tslTokens : ruleset.getRules().stream()
                .map(rule -> rule.getSnippet().getAllTokens())
                .collect(Collectors.toList())) {
            System.out.println(tslTokens);
        }

//        boolean performed = ruleset.perform(context);
//        System.out.println("Performed: " + performed);
    }

}
