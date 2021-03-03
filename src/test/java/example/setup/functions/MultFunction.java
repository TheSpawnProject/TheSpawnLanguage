package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;

public class MultFunction extends TSLFunction {

    public static final MultFunction INSTANCE = new MultFunction();

    public MultFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_mult", true);
    }

    @Override
    public TSLFunction.with2Params<Number, Number> getBindingObject() {
        return (number1, number2) -> number1.doubleValue() * number2.doubleValue();
    }

}
