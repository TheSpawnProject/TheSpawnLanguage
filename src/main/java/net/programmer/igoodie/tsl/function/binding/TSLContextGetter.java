package net.programmer.igoodie.tsl.function.binding;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.function.TSLFunction;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public final class TSLContextGetter extends BaseFunction {

    private final TSLContext tslContext;

    public TSLContextGetter(TSLContext tslContext) {
        this.tslContext = tslContext;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return canAccess() ? tslContext : Undefined.instance;
    }

    /**
     * Makes sure only internal usage is allowed.
     * If the function is called from
     *
     * @return Whether the function is called from an internal point or an external one
     */
    private boolean canAccess() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String targetClass = TSLFunction.class.getCanonicalName();
        for (StackTraceElement traceElement : stackTrace) {
            if (traceElement.getClassName().equalsIgnoreCase(targetClass)) {
                return true;
            }
        }
        return false;
    }

}
