package example.plugin.functions;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import org.mozilla.javascript.Scriptable;

import java.time.Instant;

public class CurrentUnixFunction extends TSLFunction {

    public static final CurrentUnixFunction INSTANCE = new CurrentUnixFunction();

    @Override
    public String getName() {
        return "_currentUnix";
    }

    @Override
    public Object call(TSLContext context, Scriptable scope, Object... arguments) throws TSLExpressionException {
        return Instant.now().getEpochSecond();
    }

}
