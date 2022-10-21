package net.programmer.igoodie.plugins.spawnjs.corelib.functions;

import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.Undefined;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PrintFunction extends TSLFunction {

    public static final PrintFunction INSTANCE = new PrintFunction();

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
        String message = Arrays.stream(arguments)
                .map(Object::toString)
                .collect(Collectors.joining(" "));

        System.out.println(message);

        return Undefined.instance;
    }

}
