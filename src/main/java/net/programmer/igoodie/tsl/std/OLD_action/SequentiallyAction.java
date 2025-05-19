package net.programmer.igoodie.tsl.std.OLD_action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.OLD_TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Utils;

import java.util.ArrayList;
import java.util.List;

// SEQUENTIALLY <action> [AND <action>]+ [DISPLAYING msg+]?
public class SequentiallyAction extends OLD_TSLAction {

    protected List<OLD_TSLAction> actions;

    public SequentiallyAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        this.actions = new ArrayList<>();

        List<List<String>> actionChunks = Utils.splitIntoChunks(args, arg -> arg.equalsIgnoreCase("AND"));

        for (List<String> actionChunk : actionChunks) {
            if (actionChunk.isEmpty()) {
                throw new TSLSyntaxException("");
            }

//            TSLAction action = TSLParser.immediate(platform, actionChunk).parseAction();
//            this.actions.add(action);
        }
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        boolean success = true;

        for (OLD_TSLAction action : actions) {
            success &= action.perform(ctx);
        }

        return success;
    }

}
