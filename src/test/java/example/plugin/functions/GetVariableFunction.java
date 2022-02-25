package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import org.mozilla.javascript.Scriptable;

public class GetVariableFunction extends TSLFunction {

    public static final GetVariableFunction INSTANCE = new GetVariableFunction();

    @Override
    public String getName() {
        return "_getVariable";
    }

    @Override
    public Object call(TSLContext tslContext, Scriptable scope, Object... arguments) throws TSLExpressionException {
        String variableName = stringArgument(arguments, 0);
        return ExamplePlugin.VARIABLE_CACHE.get(variableName);
    }

}
