package parser;

import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

public class TSLParserTests {

    @Test
    public void shouldTokenizeSimple() {
        String script = "DROP %minecraft:|${\"diamond\"}|% ${10 * Math.random()} $smth";

        TSLParser parser = TSLParser.fromScript(script);

        for (TSLWord word : parser.parseWords()) {
            System.out.println(word);
        }

        System.out.println();

        System.out.println("TOKENS");
        for (Token token : parser.getTokens()) {
            System.out.println(token + " " + TSLLexer.VOCABULARY.getDisplayName(token.getType()));
        }
    }

}
