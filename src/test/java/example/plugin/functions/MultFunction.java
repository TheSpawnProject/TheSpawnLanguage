package example.plugin.functions;

import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;

public class MultFunction extends TSLFunction {

    public static final MultFunction INSTANCE = new MultFunction();

    @Override
    public String getName() {
        return "_mult";
    }

    @Override
    public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
        Number a = requiredArgument(TSLArguments.DOUBLE, arguments, 0);
        Number b = requiredArgument(TSLArguments.DOUBLE, arguments, 1);
        return a.doubleValue() * b.doubleValue();
    }
}
