package automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLActionSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserTests {

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

}
