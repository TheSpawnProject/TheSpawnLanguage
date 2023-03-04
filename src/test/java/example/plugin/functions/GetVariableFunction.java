package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;

public class GetVariableFunction extends TSLFunction {

    public static final GetVariableFunction INSTANCE = new GetVariableFunction();

    @Override
    public String getName() {
        return "_getVariable";
    }

    @Override
    public Object call(TSLContext tslContext, JSScope scope, Object... arguments) throws TSLExpressionException {
        String variableName = requiredArgument(TSLArguments.STRING, arguments, 0);
        return ExamplePlugin.VARIABLE_CACHE.get(variableName);
    }

}
