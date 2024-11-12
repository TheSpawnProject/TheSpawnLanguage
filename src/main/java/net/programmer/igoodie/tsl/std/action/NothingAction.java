package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;

public class NothingAction extends TSLAction {

    public NothingAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        if (args.size() != 0) {
            throw new TSLSyntaxException("Expected 0 arguments, found -> %s", args);
        }
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        return true;
    }

}
