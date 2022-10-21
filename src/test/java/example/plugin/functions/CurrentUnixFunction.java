package example.plugin.functions;

import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    @Override
    public String getName() {
        return "_currentUnix";
    }

    @Override
    public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
        return Instant.now().getEpochSecond();
    }

}
