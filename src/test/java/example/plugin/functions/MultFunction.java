package example.plugin.functions;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import org.mozilla.javascript.Scriptable;

public class MultFunction extends TSLFunction {

    public static final MultFunction INSTANCE = new MultFunction();

    @Override
    public String getName() {
        return "_mult";
    }

    @Override
    public Object call(TSLContext context, Scriptable scope, Object... arguments) throws TSLExpressionException {
        Number a = numberArgument(arguments, 0);
        Number b = numberArgument(arguments, 1);
        return a.doubleValue() * b.doubleValue();
    }
}
