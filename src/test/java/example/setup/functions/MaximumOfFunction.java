package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;

public class MaximumOfFunction extends TSLFunction {

    public static final MaximumOfFunction INSTANCE = new MaximumOfFunction();

    private MaximumOfFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_maximumOf", false);
    }

    @Override
    public TSLFunction.with2Params<Number, Number> getBindingObject() {
        return (number1, number2) ->
                Double.max(number1.doubleValue(), number2.doubleValue());
    }

}
