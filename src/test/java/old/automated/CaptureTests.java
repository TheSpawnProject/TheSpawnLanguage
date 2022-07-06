package old.automated;

import net.programmer.igoodie.legacy.parser.TSLParserOld;
import net.programmer.igoodie.legacy.runtime.TSLRulesetOld;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

public class CaptureTests {

    @Test
    public void shouldLexeCaptureParams() throws IOException {
        String script = TestUtils.loadFragment("capture_1");

        TSLParserOld tslParser = new TSLParserOld(new TheSpawnLanguage());
        TSLRulesetOld ruleset = tslParser.parse(script);
        TSLCaptureSnippet capture = ruleset.getCaptureSnippet("dropAxeWithFireAspect");

        List<TSLToken> flattenedCapture = capture.flatten(new TSLPlainWord(0, 0, "15"));
        System.out.println(flattenedCapture);
    }

}
