package unit;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLGroup;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.std.action.WaitAction;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TSLParserTests {

    @Test
    public void shouldTokenizeSimple() {
        String script = "PRINT %namespace:\\\\|${\"item\"}|% ${10 * Math.random()} $smth";

        TSLParser parser = TSLParser.fromScript(script);
        parser.parseWords().forEach(System.out::println);

        System.out.println();

        System.out.println("TOKENS");
        for (Token token : parser.readAllTokens()) {
            System.out.println(token + " " + TSLLexer.VOCABULARY.getDisplayName(token.getType()));
        }
    }

    @Test
    public void shouldParseCapturesCorrectly() {
        String script = """
                $summonMob(mobId, name) = (
                    SUMMON {{mobId}}
                    DISPLAYING %[{text:"| {{name}} | spawned!", color:"blue"}]%
                )
                
                $summonMobWithItem(mobId, name, itemId) = (
                    SUMMON {{mobId}} ~ ~ ~ %{HandItems:[{id:"| {{itemId}} |",Count:1b},{}]}%
                    DISPLAYING %[{text:"| {{name}} | spawned!", color:"blue"}]%
                )
                
                EITHER $summonMob(zombie, Zombie)
                 OR $summonMobWithItem(skeleton, Skeleton, minecraft:bow)
                 OR $summonMob(minecraft:bat, Bat)
                 OR $summonMob(minecraft:blaze, Blaze)
                 ON Twitch Chat Message
                 WITH message PREFIX %!mob%
                """;

        TSLParser parser = TSLParser.fromScript(script);

        for (TSLWord word : parser.parseWords()) {
            System.out.println(word.getSource());
        }
    }

    @Test
    public void shouldParseGroupsCorrectly() {
        String script = "%Testing my expressions, | ${Math.random()} |%";

        TSLParser parser = TSLParser.fromScript(script);
        TSLWord word = parser.parseWords().get(0);

        TSLPlatform platform = new TSLPlatform("Dummy Platform", 1.0f);
        TSLEventContext ctx = new TSLEventContext(platform, "Foo Bar");

        platform.pushExpressionEvaluator(expression -> expression);

        if (word instanceof TSLGroup group) {
            System.out.println(group.evaluate(ctx));
        }
    }

    @Test
    public void shouldInitiateWaitAction() throws TSLPerformingException, TSLSyntaxException {
        String script = "#*WAIT*# 2 seconds";

        TSLParser parser = TSLParser.fromScript(script);
        List<TSLWord> actionArgs = parser.parseWords();

        TSLPlatform platform = new TSLPlatform("Dummy Platform", 1.0f);
        platform.initializeStd();

        WaitAction waitAction = (WaitAction) platform.getActionDefinition("WAIT").orElseThrow()
                .createAction(actionArgs.stream().map(Either::<TSLWord, TSLAction>left).toList());

        TSLEventContext ctx = new TSLEventContext(platform, "Dummy Event");
        waitAction.perform(ctx);
    }

}
