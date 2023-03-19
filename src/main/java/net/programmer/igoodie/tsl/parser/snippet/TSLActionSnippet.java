package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// [DROP] [apple 2]
public class TSLActionSnippet extends TSLSnippet {

    protected TSLPlainWord actionName;
    protected List<TSLToken> actionTokens;

    protected TSLAction actionDefinition;

    public TSLActionSnippet(TSLAction actionDefinition, TSLCaptureCall captureCall, List<TSLToken> leadingTokens,
                            TSLPlainWord actionName, List<TSLToken> actionTokens) {
        super(flatTokens(captureCall, leadingTokens));
        this.actionName = actionName;
        this.actionTokens = actionTokens;
        this.actionDefinition = actionDefinition;
    }

    public TSLActionSnippet(TSLAction actionDefinition, TSLPlainWord actionName, List<TSLToken> actionTokens) {
        super(flatTokens(actionName, actionTokens));
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

    public static List<TSLToken> flatten(List<TSLToken> tokens, Map<String, TSLCaptureSnippet> captureSnippets) {
        List<TSLToken> flattened = new LinkedList<>();

        for (TSLToken token : tokens) {
            if (token instanceof TSLCaptureCall) {
                try {
                    TSLCaptureCall captureCall = (TSLCaptureCall) token;
                    TSLCaptureSnippet captureSnippet = captureSnippets.get(captureCall.getCaptureName());
                    if (captureSnippet == null) {
                        throw new TSLSyntaxError("Capture not defined with name -> " + captureCall.getCaptureName(), captureCall);
                    }
                    List<TSLToken> flattenedCapture = captureSnippet.fill(captureSnippets, captureCall.getArgs());
                    flattened.addAll(flattenedCapture);

                } catch (IllegalArgumentException e) {
                    // TODO: Fix that non-sense message
                    throw new TSLSyntaxError("Capture arguments MUST NOT contain multiple tokens at once.", token).causedBy(e);
                }

            } else {
                flattened.add(token);
            }
        }

        return flattened;
    }

}
