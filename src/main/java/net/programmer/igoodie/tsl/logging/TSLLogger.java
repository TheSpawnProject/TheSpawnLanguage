package net.programmer.igoodie.tsl.logging;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.util.ExpressionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TSLLogger {

    private static final Pattern FORMAT_ARG_PATTERN = Pattern.compile("\\{(?<index>\\d*)}");

    private final @NotNull Logger underlyingLogger;

    private TSLLogger(@NotNull Logger underlyingLogger) {
        this.underlyingLogger = underlyingLogger;
    }

    public void trace(String message, Object... arguments) {
        log(TSLLogLevel.TRACE, formatMessage(message, arguments));
    }

    public void debug(String message, Object... arguments) {
        log(TSLLogLevel.DEBUG, formatMessage(message, arguments));
    }

    public void info(String message, Object... arguments) {
        log(TSLLogLevel.INFO, formatMessage(message, arguments));
    }

    public void warn(String message, Object... arguments) {
        log(TSLLogLevel.WARN, formatMessage(message, arguments));
    }

    public void error(String message, Object... arguments) {
        log(TSLLogLevel.ERROR, formatMessage(message, arguments));
    }

    public void fatal(String message, Object... arguments) {
        log(TSLLogLevel.FATAL, formatMessage(message, arguments));
    }

    public void log(TSLLogLevel level, String message, Object... arguments) {
        underlyingLogger.log(level, formatMessage(message, arguments));
    }

    private String formatMessage(String message, Object... arguments) {
        ArrayAccessor<Object> argumentAccessor = ArrayAccessor.of(arguments);
        AtomicInteger cursorArgumentIndex = new AtomicInteger();

        String formatted = String.format(message, arguments);

        return ExpressionUtils.replacePattern(formatted, FORMAT_ARG_PATTERN, (placeholder, matcher) -> {
            String indexGroup = matcher.group("index");

            int argumentIndex = indexGroup == null || indexGroup.isEmpty()
                    ? cursorArgumentIndex.getAndIncrement()
                    : Integer.parseInt(indexGroup);

            return argumentAccessor.get(argumentIndex).orElse("{" + placeholder + "}").toString();
        });
    }

    /* ------------------------------------- */

    public static TSLLogger createLogger(TSLPlugin plugin, TSLLogHandler handler) {
        return createLogger(plugin.getDescriptor().getPluginId(), handler);
    }

    public static TSLLogger createLogger(String namespace, TSLLogHandler handler) {
        Logger logger = Logger.getLogger(namespace);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(TSLLogLevel.DEBUG);

        return new TSLLogger(logger);
    }

}
