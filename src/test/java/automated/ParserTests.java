package automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.TSLActionSnippet;
import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ParserTests {

    @Test
    public void shouldParseImports() throws IOException, URISyntaxException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();

        tsl.getPluginManager().loadPlugin(new ExamplePlugin());

        TSLParser parser = new TSLParser(tsl);
        TSLRuleset ruleset = parser.parse(TestUtils.scriptPath("parser.test.tsl"));

        Assertions.assertNotNull(ruleset.getImports().get("ExampleLib"));
        Assertions.assertNotNull(ruleset.getImports().get("exampleplugin"));
    }

    @Test
    public void foo() {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        tsl.getPluginManager().loadPlugin(new ExamplePlugin());

        TSLParser parser = new TSLParser(tsl);
        TSLRuleset ruleset = parser.parse("PRINT x DISPLAYING A B C ON Dummy Event");

        TSLRule rule = ruleset.getRules().get(0);
        TSLActionSnippet actionSnippet = rule.getSnippet().getActionSnippet();
        TSLAction actionDefinition = actionSnippet.getActionDefinition();

        @NotNull Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(actionSnippet.getActionTokens());
        List<TSLToken> actionArgs = couple.getFirst();
        TSLToken message = couple.getSecond();

        System.out.println(actionArgs);
        System.out.println(message);
    }

    @Test
    public void shouldParseCoconutsPluginExtensions() throws IOException {
//        TheSpawnLanguage tsl = new TheSpawnLanguage();
//        tsl.getPluginManager().loadPlugin(new CoconutPlugin());
//
//        TSLParser parser = new TSLParser(tsl);
//        TSLRuleset ruleset = parser.parse(TestUtils.loadTSLScript("coconutorange.rule.tsl"));
//
//        TSLRule rule = ruleset.getRules().get(0);

        TSLLexer lex = new TSLLexer(TestUtils.loadTSLScript("coconutorange.rule.tsl")).lex();
        for (TSLSnippetBuffer snippet : lex.getSnippets()) {
            System.out.println(snippet.getType());
            for (TSLToken token : snippet.getTokens()) {
                System.out.println(token);
            }
            System.out.println();
        }
    }

}
