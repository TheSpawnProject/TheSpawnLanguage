package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.LinkedList;
import java.util.List;

// [DROP] [apple 2]
public class TSLActionSnippet extends TSLSnippet {

    protected TSLPlainWord actionName;
    protected List<TSLToken> actionTokens;

    protected TSLAction actionDefinition;

    public TSLActionSnippet(TSLRuleset ruleset, TSLAction actionDefinition, TSLPlainWord actionName, List<TSLToken> actionTokens) {
        super(ruleset, flatTokens(actionName, actionTokens));
        this.actionName = actionName;
        this.actionTokens = actionTokens;
        this.actionDefinition = actionDefinition;
    }

    public TSLPlainWord getActionNameToken() {
        return actionName;
    }

    public List<TSLToken> getActionTokens() {
        return actionTokens;
    }

    /* -------------------------- */

    public TSLAction getActionDefinition() {
        return actionDefinition;
    }

    /* -------------------------- */

    public static List<TSLToken> flatten(TSLRuleset ruleset, List<TSLToken> tokens) {
        List<TSLToken> flattened = new LinkedList<>();

        for (TSLToken token : tokens) {
            if (token instanceof TSLCaptureCall) {
                try {
                    TSLCaptureCall captureCall = (TSLCaptureCall) token;
                    TSLCaptureSnippet captureSnippet = ruleset.getCaptureSnippet(captureCall);
                    List<TSLToken> flattenedCapture = captureSnippet.flattenInline(captureCall.getArgs());
                    flattened.addAll(flattenedCapture);
                } catch (IllegalArgumentException e) {
                    throw new TSLSyntaxError("Capture arguments MUST NOT contain multiple tokens at once. " + e.getMessage(), token);
                }

            } else {
                flattened.add(token);
            }
        }

        return flattened;
    }

}
