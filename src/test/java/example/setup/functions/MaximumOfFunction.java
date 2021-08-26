package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;

public class MaximumOfFunction extends TSLFunction {

    public static final MaximumOfFunction INSTANCE = new MaximumOfFunction();

    private MaximumOfFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_maximumOf");
    }

    @Override
    public Object calculate(Object... arguments) throws TSLExpressionException {
        Number a = numberArgument(arguments, 0);
        Number b = numberArgument(arguments, 1);
        return Double.max(a.doubleValue(), b.doubleValue());
    }

}
