package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Utils;

import java.util.ArrayList;
import java.util.List;

// SEQUENTIALLY <action> [AND <action>]+ [DISPLAYING msg+]?
public class SequentiallyAction extends TSLAction {

    protected List<TSLAction> actions;

    public SequentiallyAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        this.actions = new ArrayList<>();

        List<List<String>> actionChunks = Utils.splitIntoChunks(args, arg -> arg.equalsIgnoreCase("AND"));

        for (List<String> actionChunk : actionChunks) {
            if (actionChunk.size() == 0) {
                throw new TSLSyntaxException("");
            }

            String actionName = actionChunk.get(0);

            TSLAction.Supplier<?> actionDefinition = platform.getActionDefinition(actionName).orElseThrow(() -> new TSLSyntaxException("Unknown action -> {}", actionName));

            List<String> actionArgs = actionChunk.subList(1, actionChunk.size());
            TSLAction action = actionDefinition.generate(platform, actionArgs);

            this.actions.add(action);
        }
    }

    @Override
    public boolean perform(TSLEventContext ctx) {
        boolean success = true;

        for (TSLAction action : actions) {
            success &= action.perform(ctx);
        }

        return success;
    }

}
