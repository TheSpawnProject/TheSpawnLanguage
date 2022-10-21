package automated;

import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.plugins.grammar.events.ManualTriggerEvent;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.ScriptableObject;
import util.TestUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class JSLibrariesTest {

    public static class MathLibrary extends TSLFunctionLibrary {

        public MathLibrary(String name) {
            super(ExamplePlugin.PLUGIN_INSTANCE, name);
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
                public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
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
        TSLContext dummyContext = new TSLContext(tsl);

        ExamplePlugin plugin = new ExamplePlugin() {
            @Override
            public void registerFunctionLibraries(TSLRegistry<TSLFunctionLibrary> registry) {
                super.registerFunctionLibraries(registry);
                registry.register(new MathLibrary("tslmath"));
                registry.register(new MathLibrary("tslmath2"));
            }
        };
        tsl.getPluginManager().loadPlugin(plugin);

        JSScope scope = jsEngine.getGlobalScope().fork();
        dummyContext.setJsScope(scope);

        Map<String, String> imports = new HashMap<>();
        imports.put("EP", plugin.getManifest().getPluginId());
        dummyContext.setImportedPlugins(imports);

        jsEngine.loadTSLContext(scope, dummyContext);

        System.out.println(scope.debugDumpScope());

        System.out.println(jsEngine.evaluate("({ tslmath: EP.tslmath })", scope));
        System.out.println(jsEngine.evaluate("({ tslmath2: EP.tslmath2 })", scope));
        System.out.println(jsEngine.evaluate("EP.tslmath2.operations.mult(2, 5)", scope));
        System.out.println(jsEngine.evaluate("EP.tslmath2.pi", scope));
    }

    @Test
    public void shouldSupportLet() throws IOException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        JSEngine jsEngine = tsl.getJsEngine();
        TSLContext context = new TSLContext(tsl);

        tsl.getPluginManager().loadPlugin(new ExamplePlugin());

        context.setEvent(ManualTriggerEvent.INSTANCE);
        context.setJsScope(jsEngine.getGlobalScope().fork());

        TSLRuleset ruleset = new TSLParser(tsl).parse(TestUtils.loadTSLScript("rhino.tests.tsl"));
        ruleset.perform(context);
    }

    @Test
    public void typescriptStuff() throws IOException, URISyntaxException {
        TheSpawnLanguage tsl = new TheSpawnLanguage();
        File rulesetFile = new File(TestUtils.resourceURL("typescript/myRuleset.tsl").toURI());

        tsl.getPluginManager().loadPlugin(new ExamplePlugin()); // for the PRINT action

        TSLContext context = new TSLContext(tsl);
        context.setEvent(ManualTriggerEvent.INSTANCE);
        GoodieObject eventArgument = new GoodieObject();
        GoodieObject debugData = new GoodieObject();
        debugData.put("Foo", 123);
        debugData.put("Bar", 456);
        eventArgument.put("debug_data", debugData);
        context.setEventArguments(eventArgument);

        TSLRuleset ruleset = new TSLParser(tsl).parse(rulesetFile);

        ruleset.perform(context);
    }

}
