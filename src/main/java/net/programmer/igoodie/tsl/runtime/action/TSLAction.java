package net.programmer.igoodie.tsl.runtime.action;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Pair;
import net.programmer.igoodie.tsl.util.PatternReplacer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TSLAction {

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    protected final TSLPlatform platform;
    protected List<String> message = Collections.emptyList();

    public TSLAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        this.platform = platform;
    }

    public List<String> getMessage() {
        return message;
    }

    public List<String> getCalculatedMessage(TSLEventContext ctx) {
        return message.stream()
                .map(word -> replaceExpressions(word, ctx))
                .collect(Collectors.toList());
    }

    protected List<String> consumeMessagePart(List<String> args) {
        Pair<List<String>, List<String>> parts = splitDisplaying(args);
        this.message = parts.getRight();
        return parts.getLeft();
    }

    protected Pair<List<String>, List<String>> splitDisplaying(List<String> args) {
        int index = IntStream.range(0, args.size())
                .filter(i -> args.get(args.size() - i - 1).equalsIgnoreCase("DISPLAYING"))
                .map(i -> args.size() - i - 1)
                .findFirst().orElse(-1);

        if (index == -1) {
            return new Pair<>(args, Collections.emptyList());
        }

        List<String> actionPart = args.subList(0, index);
        List<String> displayingPart = args.subList(index + 1, args.size());
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
        T generate(TSLPlatform platform, List<String> args) throws TSLSyntaxException;
    }

    @FunctionalInterface
    public interface ExpressionEvaluator {
        Optional<?> evaluate(String expr, TSLEventContext ctx);
    }

    protected int parseInt(String string) throws TSLSyntaxException {
        try {return Integer.parseInt(string);} catch (NumberFormatException e) {
            throw new TSLSyntaxException("Expected an integer, found instead -> %s", string);
        }
    }

}
