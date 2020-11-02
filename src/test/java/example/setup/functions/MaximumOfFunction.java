package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.Lambda2;

public class MaximumOfFunction extends TSLFunction {

    public static final MaximumOfFunction INSTANCE = new MaximumOfFunction();

    private MaximumOfFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_maximumOf", false);
    }

    @Override
    public Object getBindingObject() {
        return (Lambda2<Number, Number>) (a, b) -> {
            if (a.doubleValue() > b.doubleValue())
                return a;
            return b;
        };
    }

}
