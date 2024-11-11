package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SequentiallyAction extends TSLAction {

    protected List<TSLAction> actions;

    public SequentiallyAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeMessagePart(args);

        this.actions = new ArrayList<>();

        for (List<String> actionRaw : splitByPredicate(args, arg -> arg.equalsIgnoreCase("AND"))) {
            if (actionRaw.size() == 0) {
                throw new TSLSyntaxException("");
            }

            String actionName = actionRaw.get(0);

            TSLAction.Supplier<?> actionDefinition = platform.getActionDefinition(actionName)
                    .orElseThrow(() -> new TSLSyntaxException("Unknown action -> {}", actionName));

            List<String> actionArgs = actionRaw.subList(1, actionRaw.size());
            TSLAction action = actionDefinition.generate(platform, actionArgs);

            this.actions.add(action);
        }
    }

    // TODO: Use this on EITHER instead
//    protected List<String> consumeAllMessagePart(List<String> args) throws TSLSyntaxException {
//        int indexAll = IntStream.range(0, args.size())
//                .filter(i -> args.get(args.size() - i - 1).equalsIgnoreCase("ALL"))
//                .map(i -> args.size() - i - 1)
//                .findFirst().orElse(-1);
//
//        if (indexAll == -1) return args;
//
//        ListAccessor.of(args).get(indexAll + 1)
//                .filter(arg -> arg.equalsIgnoreCase("DISPLAYING"))
//                .orElseThrow(() -> new TSLSyntaxException("Expected 'DISPLAYING' after 'ALL'"));
//
//        this.message = args.subList(indexAll + 2, args.size());
//
//        return args.subList(0, indexAll + 2);
//    }

    protected <V> List<List<V>> splitByPredicate(List<V> input, Predicate<V> delimiterPredicate) {
        List<List<V>> result = new ArrayList<>();
        List<V> currentList = new ArrayList<>();

        for (V element : input) {
            if (delimiterPredicate.test(element)) {
                result.add(new ArrayList<>(currentList));
                currentList.clear();
            } else {
                currentList.add(element);
            }
        }

        if (!currentList.isEmpty()) {
            result.add(currentList);
        }

        return result;
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
