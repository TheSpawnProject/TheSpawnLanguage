package automated;

import com.google.common.io.Resources;
import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.TSLSnippet;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class ParserTests {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
        TSL.loadPlugin(new ExamplePlugin());
    }

    @Test
    public void foo() throws IOException {
        List<String> lines = Resources.readLines(Resources.getResource("tsl/sample.tsl"), StandardCharsets.UTF_8);
        String script = String.join("\n", lines);

        System.out.println(script);
        System.out.println("/--------------------------------/");

        TSLParser parser = new TSLParser(TSL);
        TSLRuleset ruleset = parser.parse(script);

        System.out.println("/--------------------------------/");

        System.out.println(ruleset.getTags());
        System.out.println(ruleset.getSquashedAttributes());
    }

}
