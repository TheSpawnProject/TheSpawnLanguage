package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;

public class GetVariableFunction extends TSLFunction {

    public static final GetVariableFunction INSTANCE = new GetVariableFunction();

    private GetVariableFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_getVariable", false);
    }

    @Override
    public TSLFunction.with1Param<String> getBindingObject() {
        return ExamplePlugin.VARIABLE_CACHE::get;
    }

}
