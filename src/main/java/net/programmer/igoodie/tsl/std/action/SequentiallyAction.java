package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParser;
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

            TSLAction action = new TSLParser(platform, actionChunk).parseAction();
            this.actions.add(action);
        }
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        boolean success = true;

        for (TSLAction action : actions) {
            success &= action.perform(ctx);
        }

        return success;
    }

}
