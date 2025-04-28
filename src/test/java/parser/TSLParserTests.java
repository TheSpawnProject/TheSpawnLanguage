package parser;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.interpreter.TSLWordInterpreter;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.word.TSLGroup;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.std.action.WaitAction;
import net.programmer.igoodie.tsl.util.structure.Either;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

public class TSLParserTests {

    @Test
    public void shouldTokenizeSimple() {
        String script = "PRINT %namespace:|${\"item\"}|% ${10 * Math.random()} $smth";

        TSLParser parser = TSLParser.fromScript(script);
        parser.parseWords().forEach(System.out::println);

        System.out.println();

        System.out.println("TOKENS");
        for (Token token : parser.getTokens()) {
            System.out.println(token + " " + TSLLexer.VOCABULARY.getDisplayName(token.getType()));
        }
    }

    @Test
    public void shouldParseGroupsCorrectly() {
        String script = "%Testing my expressions, | ${Math.random()} |%";

        TSLParser parser = TSLParser.fromScript(script);
        TSLWord word = parser.parseWords().get(0);

        TSLPlatform platform = new TSLPlatform("Dummy Platform", 1.0f);
        TSLEventContext ctx = new TSLEventContext(platform, "Foo Bar");

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
        WaitAction waitAction = new WaitAction(platform,
                actionArgs.stream().map(Either::<TSLWord, TSLAction>left).toList());

        TSLEventContext ctx = new TSLEventContext(platform, "Dummy Event");
        waitAction.perform(ctx);
    }

}
