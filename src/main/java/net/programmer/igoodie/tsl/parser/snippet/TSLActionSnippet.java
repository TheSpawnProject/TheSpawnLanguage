package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.LinkedList;
import java.util.List;

// [DROP apple 2]
public class TSLActionSnippet extends TSLSnippet {

    public TSLActionSnippet(TSLRuleset ruleset, List<TSLToken> tokens) {
        super(ruleset, tokens);
    }

    public List<TSLToken> getTokens() {
        return getAllTokens();
    }

    /* -------------------------- */

    public List<TSLToken> flatten() {
        LinkedList<TSLToken> flattened = new LinkedList<>();
        TSLTokenizer tokenizer = new TSLTokenizer();

        for (TSLToken token : allTokens) {
            System.out.println("- " + token);

            if (token instanceof TSLCaptureCall) {
                TSLCaptureCall captureCall = (TSLCaptureCall) token;
                List<TSLToken> tokenizedArguments = TSLTokenizer.tokenizeAll(captureCall);
                System.out.println("Args: " + captureCall.getArgs());
                System.out.println("Tokenized: " + tokenizedArguments);

                TSLCaptureSnippet referredCapture = ruleset.getCaptureSnippet(captureCall);
                List<TSLToken> replaced = referredCapture.replaceParameters(tokenizedArguments);
                System.out.println("Replaced: " + replaced);

                flattened.addAll(replaced);

            } else {
                flattened.add(token);
            }
        }

        return flattened;
    }

}
