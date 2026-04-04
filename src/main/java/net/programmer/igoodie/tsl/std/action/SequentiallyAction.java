package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * SEQUENTIALLY <action>
 * [AND <action>]+
 */
public class SequentiallyAction extends TSLAction {

    protected List<TSLAction> actions;

    public SequentiallyAction(List<TSLClause> sourceArguments) throws TSLSyntaxException {
        super(sourceArguments);
    }

    @Override
    public void parseArguments(TSLPlatform platform, List<TSLClause> arguments) throws TSLSyntaxException {
        this.actions = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            TSLClause arg = arguments.get(i);

            // TODO: Fix
//            if (i % 2 == 0) {
//                TSLAction action = arg.expectAction();
//                this.actions.add(action);
//                continue;
//            }

            TSLToken keywordAnd = arg.getToken().orElseThrow();

            if (!TSLPlainWord.isKeyword(keywordAnd, "AND")) {
                throw new TSLSyntaxException("Expected an AND delimiter between actions.");
            }
        }

        // TODO: Fix
//        this.actions.forEach(action -> parseArguments_OLD(platform));

        if (this.sourceArguments.size() % 2 != 1) {
            throw new TSLSyntaxException("Expected an action, after AND");
        }
    }

    @Override
    public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
        for (TSLAction action : this.actions) {
            action.perform(ctx);
        }

        return Collections.emptyList();
    }

}
