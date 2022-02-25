package net.programmer.igoodie.tsl.function.binding;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public abstract class JSFunctionBinding extends BaseFunction {

    public abstract String getName();

    public abstract Calculator getCalculator();

    @Override
    public String getFunctionName() {
        return getName();
    }

    @Override
    public final Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        TSLContextGetter contextGetter = (TSLContextGetter) scope.get("__context", scope);
        TSLContext tslContext = (TSLContext) contextGetter.call(cx, scope, thisObj, args);
        return getCalculator().calculate(tslContext, scope, args);
    }

    @FunctionalInterface
    public interface Calculator {
        Object calculate(TSLContext tslContext, Scriptable scope, Object... arguments) throws TSLExpressionException;
    }

}
