package example.setup.functions;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.Lambda0;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    private CurrentUnixFunction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "_currentUnix", false);
    }

    @Override
    public Object getBindingObject() {
        return (Lambda0) () -> Instant.now().getEpochSecond();
    }

}
