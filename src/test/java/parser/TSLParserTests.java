package parser;

import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    public void shouldInitiateWaitAction() {
        String script = "#*WAIT*# \n 10 seconds";

        TSLParser parser = TSLParser.fromScript(script);

        List<TSLWord> words = parser.parseWords();
        System.out.println(words.size() + " words");
        words.forEach(System.out::println);
    }

}
