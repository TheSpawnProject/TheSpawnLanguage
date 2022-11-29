package net.programmer.igoodie.tsl.util;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import org.jetbrains.annotations.Nullable;

public class AccessUtils {

    @Nullable
    public static Class<?> accessedFromClass(Class<?> target) {
        try {
            ArrayAccessor<StackTraceElement> stackTrace =
                    ArrayAccessor.of(Thread.currentThread().getStackTrace());

            for (int i = 0; ; i++) {
                StackTraceElement traceElement = stackTrace.get(i).orElse(null);
                if (traceElement == null) return null;

                if (traceElement.getClassName().equals(target.getCanonicalName())) {
                    StackTraceElement nextElement = stackTrace.get(i + 1).orElse(null);
                    if (nextElement == null) return null;
                    return Class.forName(nextElement.getClassName());
                }
            }

        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
