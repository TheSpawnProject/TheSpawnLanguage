package automated;

import com.google.common.io.Resources;
import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLExpression;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TestFiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ParserTests {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
        TSL.loadPlugin(new ExamplePlugin());
    }

    @Test
    public void foo() throws IOException {
        String script = TestFiles.loadTSLScript("sample.tsl");

        System.out.println(script);
        System.out.println("/--------------------------------/");

        TSLParser parser = new TSLParser(TSL);
        TSLRuleset ruleset = parser.parse(script);

        System.out.println("/--------------------------------/");

        System.out.println("Tags: " + ruleset.getTags());
        System.out.println("Attrs: " + ruleset.getAttributes());

        System.out.println(ruleset.getRules());
    }

}
