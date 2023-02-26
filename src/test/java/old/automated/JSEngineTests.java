//package old.automated;
//
//import example.plugin.ExamplePlugin;
//import example.plugin.event.DummyEvent;
//import net.programmer.igoodie.goodies.runtime.GoodieObject;
//import net.programmer.igoodie.tsl.TheSpawnLanguage;
//import net.programmer.igoodie.tsl.function.JSEngine;
//import net.programmer.igoodie.tsl.function.scope.JSScope;
//import net.programmer.igoodie.tsl.parser.TSLParser;
//import net.programmer.igoodie.tsl.runtime.TSLContext;
//import net.programmer.igoodie.tsl.runtime.TSLRuleset;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mozilla.javascript.EcmaError;
//import util.TestUtils;
//
//import java.io.IOException;
//
//public class JSEngineTests {
//
//    @Test
//    public void shouldEvaluateExpr() throws IOException {
//        TheSpawnLanguage language = new TheSpawnLanguage();
//        language.getPluginManagerOld().loadPlugin(new ExamplePlugin());
//
//        String tslScript = TestUtils.loadTSLScript("sample.expr.tsl");
//
//        TSLParser parser = new TSLParser(language);
//        TSLRuleset ruleset = parser.parse(tslScript);
//
//        GoodieObject eventArguments = new GoodieObject();
//        eventArguments.put("donation", 105);
//
//        TSLContext tslContext = new TSLContext(language);
//        tslContext.setEvent(DummyEvent.INSTANCE);
//        tslContext.setEventArguments(eventArguments);
//
//        ruleset.perform(tslContext);
//    }
//
//    @Test
//    public void shouldEvaluateRunCalls() {
//        TheSpawnLanguage language = new TheSpawnLanguage();
//
//        GoodieObject eventArguments = new GoodieObject();
//        eventArguments.put("donation", 100.00d);
//
//        JSEngine jsEngine = language.getJsEngine();
//        jsEngine.defineConst("PI", 3.14);
////        jsEngine.loadFunction(MaximumOfFunction.INSTANCE);
//
//        TSLContext tslContext = new TSLContext(language);
//        tslContext.setJsScope(jsEngine.getGlobalScope().fork());
//        tslContext.setEventArguments(eventArguments);
//
//        String script = "runScript('./src/test/resources/rhino/module.test.js')";
//
//        String evaluation = jsEngine.evaluate(script, tslContext);
//        System.out.println("Evalutation = " + evaluation);
//    }
//
//    @Test
//    public void shouldForkScopes() {
//        TheSpawnLanguage tsl = new TheSpawnLanguage();
//
//        JSEngine jsEngine = new JSEngine(tsl);
//        JSScope scope1 = jsEngine.getGlobalScope().fork();
//        JSScope scope2 = scope1.fork();
//        JSScope scope3 = scope2.fork();
//
//        jsEngine.evaluate("const varInScope1 = true", scope1);
//        jsEngine.evaluate("const varInScope2 = true", scope2);
//        jsEngine.evaluate("const varInScope3 = true", scope3);
//
//        // varInScope1 should be accessible on every Scope
//        Assertions.assertDoesNotThrow(() -> jsEngine.evaluate("varInScope1", scope1));
//        Assertions.assertDoesNotThrow(() -> jsEngine.evaluate("varInScope1", scope2));
//        Assertions.assertDoesNotThrow(() -> jsEngine.evaluate("varInScope1", scope3));
//
//        // varInScope2 should NOT be accessible on scope1
//        Assertions.assertThrows(EcmaError.class, () -> jsEngine.evaluate("varInScope2", scope1));
//        Assertions.assertDoesNotThrow(() -> jsEngine.evaluate("varInScope2", scope2));
//        Assertions.assertDoesNotThrow(() -> jsEngine.evaluate("varInScope2", scope3));
//
//        // varInScope3 should be accessible ONLY on scope3
//        Assertions.assertThrows(EcmaError.class, () -> jsEngine.evaluate("varInScope3", scope1));
//        Assertions.assertThrows(EcmaError.class, () -> jsEngine.evaluate("varInScope3", scope2));
//        Assertions.assertDoesNotThrow(() -> jsEngine.evaluate("varInScope3", scope3));
//    }
//
//}
