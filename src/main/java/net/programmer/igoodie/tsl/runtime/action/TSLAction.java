package net.programmer.igoodie.tsl.runtime.action;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Pair;
import net.programmer.igoodie.tsl.util.PatternReplacer;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class TSLAction {

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    protected List<String> message = Collections.emptyList();

    public TSLAction(List<String> args) throws TSLSyntaxException {}

    public List<String> getMessage() {
        return message;
    }

    public List<String> getCalculatedMessage(TSLEventContext ctx) {
        return message.stream()
                .map(word -> replaceExpressions(word, ctx))
                .collect(Collectors.toList());
    }

    protected Pair<List<String>, List<String>> splitDisplaying(List<String> args) {
        List<String> actionPart = new ArrayList<>();
        List<String> displayingPart = new ArrayList<>();

        Iterator<String> iterator = args.iterator();

        while (iterator.hasNext()) {
            String arg = iterator.next();
            if (arg.equalsIgnoreCase("DISPLAYING"))
                break;
            actionPart.add(arg);
        }

        iterator.forEachRemaining(displayingPart::add);

        return new Pair<>(actionPart, displayingPart);
    }

    public abstract boolean perform(TSLEventContext ctx);

    public final String replaceExpressions(String input, TSLEventContext ctx) {
        return PatternReplacer.replaceMatches(EXPRESSION_PATTERN, input, (matcher, matchIndex) -> {
            String expression = matcher.group(1);
            return ctx.getPlatform()
                    .getExpressionEvaluator(expression)
                    .flatMap(evaluator -> evaluator.evaluate(expression, ctx))
                    .map(Objects::toString)
                    .orElse(null);
        });
    }

    @FunctionalInterface
    public interface Supplier<T extends TSLAction> {
        T generate(List<String> args) throws TSLSyntaxException;
    }

    @FunctionalInterface
    public interface ExpressionEvaluator {
        Optional<?> evaluate(String expr, TSLEventContext ctx);
    }

}
