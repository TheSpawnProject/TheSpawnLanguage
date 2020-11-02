package example.setup.functions;

import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.function.Lambda0;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    private CurrentUnixFunction() {
        super("_currentUnix", false);
    }

    @Override
    public Object getBindingObject() {
        return (Lambda0) () -> Instant.now().getEpochSecond();
    }

}
