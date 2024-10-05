package net.programmer.igoodie.node;

import net.programmer.igoodie.event.TSLEventContext;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.util.ExpressionEvaluator;
import net.programmer.igoodie.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public abstract class TSLAction {

    protected List<String> message = Collections.emptyList();

    public TSLAction(List<String> args) {}

    public List<String> getMessage() {
        return message;
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

    public abstract void perform(TSLEventContext ctx);

    public final String replaceExpressions(String input, TSLEventContext ctx) {
        return ExpressionEvaluator.replaceExpressions(input,
                expression -> replaceExpression(expression, ctx));
    }

    protected String replaceExpression(String expression, TSLEventContext ctx) {
        GoodieObject eventArgs = ctx.getEventArgs();

        if (expression.equals("event"))
            return ctx.getEventName();

        if (expression.equals("message"))
            return eventArgs.getString("message")
                    .map(TSLAction::escape).orElse(null);

        if (expression.equals("message_unescaped"))
            return eventArgs.getString("message").orElse(null);

        if (expression.equals("title"))
            return eventArgs.getString("title").orElse(null);

        if (expression.equals("actor"))
            return eventArgs.getString("actor").orElse(null);

        if (expression.equals("streamer"))
            return eventArgs.getString("streamer").orElse(null);

        if (expression.equals("amount"))
            return eventArgs.getDouble("amount")
                    .filter(num -> num != 0.0)
                    .map(String::valueOf)
                    .orElse(null);

        if (expression.equals("amount_i"))
            return eventArgs.getDouble("amount")
                    .filter(num -> num != 0.0)
                    .map(Double::intValue)
                    .map(String::valueOf)
                    .orElse(null);

        if (expression.equals("amount_f"))
            return eventArgs.getDouble("amount")
                    .filter(num -> num != 0.0)
                    .map(num -> String.format("%.2f", num))
                    .orElse(null);

        if (expression.equals("currency"))
            return eventArgs.getString("currency").orElse(null);

        if (expression.equals("months"))
            return eventArgs.getInteger("months")
                    .filter(num -> num != 0)
                    .map(String::valueOf)
                    .orElse(null);

        if (expression.equals("tier"))
            return eventArgs.getInteger("tier")
                    .filter(num -> num != -1)
                    .map(num -> num == 0 ? "Prime" : String.valueOf(num))
                    .orElse(null);

        if (expression.equals("gifted"))
            return eventArgs.getBoolean("gifted")
                    .map(String::valueOf)
                    .orElse(null);

        if (expression.equals("viewers"))
            return eventArgs.getInteger("viewers")
                    .filter(num -> num != 0)
                    .map(String::valueOf)
                    .orElse(null);

        if (expression.equals("raiders"))
            return eventArgs.getInteger("raiders")
                    .filter(num -> num != 0)
                    .map(String::valueOf)
                    .orElse(null);

        if (expression.equals("date"))
            return getDateFormat("dd-MM-yyyy", TimeZone.getDefault()).format(new Date());

        if (expression.equals("date_utc"))
            return getDateFormat("dd-MM-yyyy", TimeZone.getTimeZone("UTC")).format(new Date());

        if (expression.equals("time"))
            return getDateFormat("HH:mm:ss", TimeZone.getDefault()).format(new Date());

        if (expression.equals("time_utc"))
            return getDateFormat("HH:mm:ss", TimeZone.getTimeZone("UTC")).format(new Date());

        if (expression.equals("unix"))
            return String.valueOf(Instant.now().getEpochSecond());

        return null;
    }

    protected static String escape(String jsonString) {
        StringBuilder escapedString = new StringBuilder();

        for (char character : jsonString.toCharArray()) {
            if (character == '\'' || character == '"' || character == '\\') {
                escapedString.append("\\");
            }

            escapedString.append(character);
        }

        return escapedString.toString();
    }

    private static DateFormat getDateFormat(String format, TimeZone timezone) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timezone);
        return dateFormat;
    }

    @FunctionalInterface
    public interface Supplier<T extends TSLAction> {
        T generate(List<String> args);
    }

}
