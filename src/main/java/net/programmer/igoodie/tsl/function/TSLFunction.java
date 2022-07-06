package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.util.TypeUtilities;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.tsl.compat.LSPFeatures;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.function.binding.TSLContextGetter;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.Optional;

public abstract class TSLFunction extends BaseFunction implements LSPFeatures {

    @Override
    public final String getFunctionName() {
        return getName();
    }

    @Override
    public final Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Object result = scope.get("__context", scope);

        if (!(result instanceof TSLContextGetter)) {
            throw new TSLInternalError("Scope MUST have a context getter meta function available in order to use a TSLFunction");
        }

        TSLContextGetter contextGetter = (TSLContextGetter) result;
        TSLContext tslContext = (TSLContext) contextGetter.call(cx, scope, thisObj, args);
        return call(tslContext, scope, args);
    }

    public abstract String getName();

    public abstract Object call(TSLContext context, Scriptable scope, Object... arguments) throws TSLExpressionException;

    /* --------------------------------- */

    protected String stringArgument(Object[] args, int index) {
        return stringArgumentOpt(args, index).orElseThrow(
                () -> new TSLExpressionException("Expected string argument at index:" + index));
    }

    protected Optional<String> stringArgumentOpt(Object[] args, int index) {
        Object element = ArrayAccessor.of(args).get(index);
        return element instanceof String
                ? Optional.of(((String) element))
                : Optional.empty();
    }

    protected Number numberArgument(Object[] args, int index) {
        return numberArgumentOpt(args, index).orElseThrow(
                () -> new TSLExpressionException("Expected number argument at index:" + index));
    }

    protected Optional<Number> numberArgumentOpt(Object[] args, int index) {
        Object element = ArrayAccessor.of(args).get(index);
        return element != null && TypeUtilities.isNumeric(element.getClass())
                ? Optional.of(((Number) element))
                : Optional.empty();
    }

}
