package net.programmer.igoodie.plugins.spawnjs.corelib.functions;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import org.mozilla.javascript.Scriptable;

public class RequireFunction extends TSLFunction {

    public static final RequireFunction INSTANCE = new RequireFunction();

    @Override
    public String getName() {
        return "require";
    }

    @Override
    public Object call(TSLContext context, Scriptable scope, Object... arguments) throws TSLExpressionException {
        return null; // TODO
    }

}
