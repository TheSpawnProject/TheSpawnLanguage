package net.programmer.igoodie.tsl.function.binding;

import net.programmer.igoodie.tsl.exception.TSLExpressionError;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;

@FunctionalInterface
public interface TSLFunctionBinding {

    Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionError;

}
