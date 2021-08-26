package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    private CurrentUnixFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_currentUnix");
    }

    @Override
    public Object calculate(Object... arguments) throws TSLExpressionException {
        return Instant.now().getEpochSecond();
    }

}
