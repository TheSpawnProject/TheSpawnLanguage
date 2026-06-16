package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLActionNest;
import net.programmer.igoodie.tsl.runtime.TSLClause;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
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

            if (i % 2 == 1) {
                arg.expectKeyword("AND");
                continue;
            }

            TSLActionNest nest = arg.expectNest();
            TSLAction action = nest.resolveAction(platform);
            this.actions.add(action);
        }

        if (arguments.size() % 2 != 1) {
            throw new TSLSyntaxException("Expected an action, after AND");
        }
    }

    @Override
    public List<TSLToken> perform(TSLEventContext ctx) throws TSLPerformingException {
        List<TSLToken> yields = Collections.emptyList();

        for (TSLAction action : this.actions) {
            List<TSLToken> actionYields = action.perform(ctx);
            if (!actionYields.isEmpty()) yields = actionYields;
        }

        return yields;
    }

}
