package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.List;

// TODO: FOR <N> TIMES
// TODO: FOR i FROM <N> TO <N> [INCREASING <N>]?
public class ForAction extends TSLAction {

    protected String variableName;
    protected int from, to, inc;
    protected TSLAction action;

    public ForAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        args = consumeLoopHeader(args);

//        action = TSLParser.immediate(platform, args).parseAction();
    }

    protected List<String> consumeLoopHeader(List<String> tokens) throws TSLSyntaxException {
        ListAccessor<String> tokenAccessor = ListAccessor.of(tokens);

        if (tokenAccessor.get(1).filter(t -> t.equalsIgnoreCase("TIMES")).isPresent()) {
            this.from = 1;
            this.to = parseInt(tokenAccessor.get(0).orElse(null));
            this.inc = 1;
            return tokens.subList(2, tokens.size());
        }

        if (tokenAccessor.get(1).filter(t -> t.equalsIgnoreCase("FROM")).isPresent()) {
            int headerEndIndex = 4;

            this.variableName = tokenAccessor.get(0)
                    .orElseThrow(() -> new TSLSyntaxException("Expected variable name"));

            this.from = parseInt(tokenAccessor.get(2).orElse(null));

            tokenAccessor.get(3).filter(t -> t.equalsIgnoreCase("TO"))
                    .orElseThrow(() -> new TSLSyntaxException("Expected 'TO'"));
            this.to = parseInt(tokenAccessor.get(4).orElse(null));

            if (tokenAccessor.get(5).filter(t -> t.equalsIgnoreCase("INCREASING")).isPresent()) {
                this.inc = parseInt(tokenAccessor.get(6).orElse(null));
                headerEndIndex += 2;
            } else {
                this.inc = 1;
            }

            return tokens.subList(headerEndIndex + 1, tokens.size());
        }

        throw new TSLSyntaxException("Malformed FOR arguments.");
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        boolean success = true;

        for (int i = from; i <= to; i += inc) {
            if (variableName != null)
                ctx.getPerformState().put(variableName, i);
            success &= action.perform(ctx);
        }

        if (variableName != null)
            ctx.getPerformState().remove(variableName);

        return success;
    }

}
