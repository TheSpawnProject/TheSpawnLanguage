package old.automated;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

public class CaptureTests {

    @Test
    public void shouldLexeCaptureParams() throws IOException {
        String script = TestUtils.loadFragment("capture_1");

        TSLParser tslParser = new TSLParser(new TheSpawnLanguage());
        TSLRuleset ruleset = tslParser.parse(script);
        TSLCaptureSnippet capture = ruleset.getCaptureSnippet("dropAxeWithFireAspect");

        List<TSLToken> flattenedCapture = capture.flattenInline("15");
        System.out.println(flattenedCapture);
    }

}
