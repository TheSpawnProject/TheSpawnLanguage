package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.tsl.compat.LSPFeatures;
import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLExpressionError;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.function.binding.TSLContextGetter;
import net.programmer.igoodie.tsl.function.binding.TSLFunctionBinding;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.Optional;

public abstract class TSLFunction extends BaseFunction implements TSLFunctionBinding, LSPFeatures {

    @Override
    public final String getFunctionName() {
        return getName();
    }

    @Override
    public final Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!(scope instanceof JSScope))
            return "#!USAGE_OUTSIDE_TSL!#"; // <- Usage outside JSScope is not allowed

        JSScope jsScope = (JSScope) scope;
        TSLContextGetter tslContextGetter = jsScope.meta().tslContextGetter.get().orElse(null);

        if (tslContextGetter == null) {
            throw new TSLInternalError("Scope MUST have a context getter meta function available in order to use a TSLFunction");
        }

        TSLContext tslContext = (TSLContext) tslContextGetter.call(cx, scope, thisObj, args);
        return call(tslContext, jsScope, args);
    }

    public abstract String getName();

    public abstract Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionError;

    /* --------------------------------- */

    protected <T> T requiredArgument(TSLArguments.Parser<T> parser, Object[] args, int index) {
        return optionalArgument(parser, args, index)
                .orElseThrow(() -> new TSLExpressionError("Expected " + parser.getType().getSimpleName() + " argument at index: " + index));
    }

    protected <T> Optional<T> optionalArgument(TSLArguments.Parser<T> parser, Object[] args, int index) {
        Object value = ArrayAccessor.of(args).get(index).orElse(null);
        if (value == null) return Optional.empty();

        if (parser.getType().isAssignableFrom(value.getClass())) {
            @SuppressWarnings("unchecked")
            T t = (T) value;
            return Optional.of(t);
        }

        if (value instanceof String) {
            return parser.parse(((String) value));
        }

        return Optional.empty();
    }

}
