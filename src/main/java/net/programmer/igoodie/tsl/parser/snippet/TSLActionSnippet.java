package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// [DROP] [apple 2]
public class TSLActionSnippet extends TSLSnippet {

    protected TSLString actionName;
    protected List<TSLToken> actionArgs;

    protected TSLAction actionDefinition;

    public TSLActionSnippet(TSLRuleset ruleset, TSLAction actionDefinition, TSLString actionName, List<TSLToken> actionArgs) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, actionName, actionArgs));
        this.actionName = actionName;
        this.actionArgs = actionArgs;
        this.actionDefinition = actionDefinition;
    }

    public TSLString getActionNameToken() {
        return actionName;
    }

    public List<TSLToken> getActionArgTokens() {
        return actionArgs;
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
