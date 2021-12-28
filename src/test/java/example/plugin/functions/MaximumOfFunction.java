package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import org.mozilla.javascript.Scriptable;

public class MaximumOfFunction extends TSLFunction {

    public static final MaximumOfFunction INSTANCE = new MaximumOfFunction();

    private MaximumOfFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_maximumOf");
    }

    @Override
    public Object calculate(TSLContext tslContext, Scriptable scope, Object... arguments) throws TSLExpressionException {
        Number a = numberArgument(arguments, 0);
        Number b = numberArgument(arguments, 1);
        return Double.max(a.doubleValue(), b.doubleValue());
    }

}
