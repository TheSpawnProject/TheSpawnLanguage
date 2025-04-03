package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.runtime.predicate.TSLPredicate;
import net.programmer.igoodie.tsl.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

// IF <C> THEN <A> [ELSE <A>]
public class IfAction extends TSLAction {

    protected TSLPredicate condition;
    protected TSLAction thenAction;
    protected TSLAction elseAction;

    public IfAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);

        splitIfElse(args).using((ifPart, elsePart) -> {
            splitCondition(ifPart).using((conditionArgs, bodyArgs) -> {
                this.condition = TSLParser.immediate(platform, conditionArgs).parsePredicate();
                this.thenAction = TSLParser.immediate(platform, bodyArgs).parseAction();
            });

            if (!elsePart.isEmpty()) {
                this.elseAction = TSLParser.immediate(platform, elsePart).parseAction();
            }
        });
    }

    protected Pair<List<String>, List<String>> splitIfElse(List<String> args) throws TSLSyntaxException {
        int indexElse = IntStream.range(0, args.size())
                .filter(i -> args.get(i).equalsIgnoreCase("ELSE"))
                .findFirst().orElse(-1);

        if (indexElse == -1) return new Pair<>(args, Collections.emptyList());

        List<String> elseActionChunk = args.subList(indexElse + 1, args.size());

        if (elseActionChunk.isEmpty()) {
            throw new TSLSyntaxException("Excepted an action after 'ELSE'.");
        }

        return new Pair<>(args.subList(0, indexElse), elseActionChunk);
    }

    protected Pair<List<String>, List<String>> splitCondition(List<String> args) throws TSLSyntaxException {
        int indexThen = IntStream.range(0, args.size())
                .filter(i -> args.get(i).equalsIgnoreCase("THEN"))
                .findFirst().orElse(-1);

        if (indexThen == -1) {
            throw new TSLSyntaxException("Expected 'THEN' after condition.");
        }

        return new Pair<>(args.subList(0, indexThen), args.subList(indexThen + 1, args.size()));
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        if (ctx.getPerformingRule() == null) {
            throw new TSLPerformingException("Cannot perform IF action, outside a TSL Rule.");
        }

        if (condition.test(ctx.getPerformingRule(), ctx)) {
            return thenAction.perform(ctx);

        } else if (elseAction != null) {
            return elseAction.perform(ctx);
        }

        return true;
    }

}
