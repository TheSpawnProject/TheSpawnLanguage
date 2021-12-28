package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import org.mozilla.javascript.Scriptable;

public class MultFunction extends TSLFunction {

    public static final MultFunction INSTANCE = new MultFunction();

    public MultFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_mult");
    }

    @Override
    public Object calculate(TSLContext tslContext, Scriptable scope, Object... arguments) throws TSLExpressionException {
        Number a = numberArgument(arguments, 0);
        Number b = numberArgument(arguments, 1);
        return a.doubleValue() * b.doubleValue();
    }
}
