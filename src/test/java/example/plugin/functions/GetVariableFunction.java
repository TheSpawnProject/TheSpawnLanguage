package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;

public class GetVariableFunction extends TSLFunction {

    public static final GetVariableFunction INSTANCE = new GetVariableFunction();

    private GetVariableFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_getVariable");
    }

    @Override
    public Object calculate(Object... arguments) throws TSLExpressionException {
        String variableName = stringArgument(arguments, 0);
        return ExamplePlugin.VARIABLE_CACHE.get(variableName);
    }

}
