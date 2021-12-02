package example.plugin.functions;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import org.mozilla.javascript.Scriptable;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    private CurrentUnixFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_currentUnix");
    }

    @Override
    public Object calculate(Scriptable scope, Object... arguments) throws TSLExpressionException {
        return Instant.now().getEpochSecond();
    }

}
