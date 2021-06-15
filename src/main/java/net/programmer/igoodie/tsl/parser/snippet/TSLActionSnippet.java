package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

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

    public TSLAction getActionDefinition() {
        return actionDefinition;
    }

    public TSLString getActionNameWord() {
        return actionName;
    }

    public List<TSLToken> getActionArgs() {
        return actionArgs;
    }

    /* -------------------------- */

    public static List<TSLToken> flatten(TSLRuleset ruleset, List<TSLToken> tokens) {
        List<TSLToken> flattened = new LinkedList<>();

        for (TSLToken token : tokens) {
            if (token instanceof TSLCaptureCall) {
                TSLCaptureCall captureCall = (TSLCaptureCall) token;
                System.out.println("Capture: " + captureCall.getCaptureName());
                System.out.println("Args: " + captureCall.getArgs());

                flattened.add(token);
            } else {
                flattened.add(token);
            }
        }

        return flattened;
    }

}
