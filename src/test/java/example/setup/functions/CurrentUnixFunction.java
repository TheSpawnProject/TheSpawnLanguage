package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    private CurrentUnixFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_currentUnix", false);
    }

    @Override
    public TSLFunction.withNoParams getBindingObject() {
        return () -> Instant.now().getEpochSecond();
    }

}
