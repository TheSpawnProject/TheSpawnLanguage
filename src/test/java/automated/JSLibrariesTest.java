package automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.TSLFunction;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSLibrariesTest {

    public static class MathLibrary extends TSLFunctionLibrary {

        public MathLibrary() {
            super(ExamplePlugin.PLUGIN_INSTANCE, "tslmath");
        }

        @Override
        public void composeLibrary(ScriptableObject object) {
            registerConst(object, "pi", Math.PI);
            registerModule(object, new OperationsModule());
        }

    }

    public static class OperationsModule extends TSLFunctionLibrary {

        public OperationsModule() {
            super(ExamplePlugin.PLUGIN_INSTANCE, "operations");
        }

        @Override
        public void composeLibrary(ScriptableObject object) {
            registerFunction(object, new TSLFunction() {
                @Override
                public String getName() {
                    return "mult";
                }

                @Override
                public Object call(TSLContext context, Scriptable scope, Object... arguments) throws TSLExpressionException {
                    try {
                        ArrayAccessor<Object> accessor = ArrayAccessor.of(arguments);
                        double number1 = (double) accessor.get(0);
                        double number2 = (double) accessor.get(1);
                        return number1 * number2;

                    } catch (ClassCastException e) {
                        throw new TSLExpressionException("Expected 2 arguments to be numbers");
                    }
                }
            });
        }

    }

    @Test
    public void shouldLoadLibrariesDuringRuntime() {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        JSEngine jsEngine = tsl.getJsEngine();

        ScriptableObject childScope = jsEngine.createChildScope();

        jsEngine.loadLibrary(childScope, "tslmath", new MathLibrary());
        jsEngine.loadLibrary(childScope, "tslmath2", new MathLibrary());

        jsEngine.loadTSLContext(childScope, new TSLContext(tsl));
        System.out.println(jsEngine.evaluate("({ tslmath: tslmath })", childScope));
        System.out.println(jsEngine.evaluate("({ tslmath2: tslmath2 })", childScope));
        System.out.println(jsEngine.evaluate("tslmath2.operations.mult(2, 5)", childScope));
        System.out.println(jsEngine.evaluate("tslmath2.pi", childScope));
    }

}
