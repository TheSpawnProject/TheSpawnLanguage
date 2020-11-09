package example.setup;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.parser.token.TSLExpression;
import net.programmer.igoodie.tsl.parser.token.TSLGroup;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.node.ActionNode;
import net.programmer.igoodie.tsl.runtime.node.EventNode;
import net.programmer.igoodie.tsl.runtime.node.PredicateNode;
import net.programmer.igoodie.tsl.util.GsonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExampleTest {

    private static TheSpawnLanguage TSL;

    @BeforeAll
    public static void init() {
        TSL = new TheSpawnLanguage();
    }

    @Test
    @Order(1)
    public void shouldLoadPlugins() {
        TSL.loadPlugin(new ExamplePlugin());
    }

    @Test
    @Order(2)
    public void shouldEvaluateExpressions() {
        ExamplePlugin.LOGGER.info(TSL.getJsEngine()
                .evaluate("_currentUnix() + ' ' + _maximumOf(5,10)", null));

        ExamplePlugin.LOGGER.info(TSL.getJsEngine()
                .evaluate("_.iff('truthy', TSL_VERSION, 0)", null));

        ExamplePlugin.LOGGER.info(TSL.getJsEngine()
                .evaluate("_.union(['A', 1, 'B'], _D.methods(_.now()))", null));

        for (int i = 0; i < 5; i++) {
            ExamplePlugin.LOGGER.info(TSL.getJsEngine()
                    .evaluate("_.randomOne('apple','banana','orange', 'tangerine')", null));
        }

    }

    @Test
    @Order(3)
    public void shouldProceedRuleTree() {
        EventNode eventNode = new EventNode(
                TSL.EVENT_REGISTRY.get("Alert Event")
        );
        PredicateNode predicateNode = new PredicateNode(
                TSL.EVENT_FIELD_REGISTRY.get("time"),
                TSL.COMPARATOR_REGISTRY.get("="),
                new TSLString(0, 0, "1234")
        );
        ActionNode actionNode = new ActionNode(
                TSL.ACTION_REGISTRY.get("PRINT")
        );

        eventNode.setNextNode(predicateNode);
        predicateNode.setNextNode(actionNode);

        List<TSLToken> actionTokens = new LinkedList<>();
        actionTokens.add(new TSLExpression(0, 0, "1 + 3"));
        actionTokens.add(new TSLString(1, 1, "Hello World"));
        actionTokens.add(new TSLGroup(2, 1, "Hey hey hey ${_currentUnix()}."));
        actionTokens.add(new TSLGroup(3, 1, "Time Arg: ${_maximumOf(time, 5)}"));

        TSLRuleset ruleset = new TSLRuleset("iGoodie", new File("Some/path/to/igoodie.tsl"));
        ruleset.getHookList().bind(new ExampleHooks());

        TSLRule rule = new TSLRule(eventNode, actionTokens);
        rule.addDecorator(TSL.DECORATOR_REGISTRY.get("suppressNotifications"),
                new TSLString(0, 0, "suppressNotifications"), new LinkedList<>());

        ruleset.addRule(rule);

        JsonObject eventArguments = new JsonObject();
        eventArguments.addProperty("event", "Alert Event");
        eventArguments.addProperty("time", 1234);

        TSLContext context = new TSLContext();
        context.setEngine(TSL.getJsEngine());
        context.setEventArguments(eventArguments);
        context.setActionTokens(actionTokens);
        context.setRule(rule);
        context.setAttributes(GsonUtils.mergeOverriding(
                ruleset.getSquashedAttributes(),
                rule.getSquashedAttributes()));

        boolean proceeded = eventNode.proceed(context);
        System.out.println("Proceeded: " + proceeded);
    }

    @Test
    @Order(3)
    public void shouldSetVariable() {
        EventNode eventNode = new EventNode(
                TSL.EVENT_REGISTRY.get("Alert Event")
        );
        ActionNode actionNode = new ActionNode(
                TSL.ACTION_REGISTRY.get("VARIABLE")
        );

        eventNode.setNextNode(actionNode);

        List<TSLToken> actionTokens = new LinkedList<>();
        actionTokens.add(new TSLString(0, 0, "my_variable"));
        actionTokens.add(new TSLString(1, 1, "12345"));

        TSLRuleset ruleset = new TSLRuleset("iGoodie", new File("Some/path/to/igoodie.tsl"));
        ruleset.getHookList().bind(new ExampleHooks());

        TSLRule rule = new TSLRule(eventNode, actionTokens);
        rule.addDecorator(TSL.DECORATOR_REGISTRY.get("suppressNotifications"),
                new TSLString(0, 0, "suppressNotifications"), new LinkedList<>());

        ruleset.addRule(rule);

        JsonObject eventArguments = new JsonObject();
        eventArguments.addProperty("event", "Alert Event");
        eventArguments.addProperty("time", 1234);

        TSLContext context = new TSLContext();
        context.setEngine(TSL.getJsEngine());
        context.setEventArguments(eventArguments);
        context.setActionTokens(actionTokens);
        context.setRule(rule);
        context.setAttributes(GsonUtils.mergeOverriding(
                ruleset.getSquashedAttributes(),
                rule.getSquashedAttributes()));

        boolean proceeded = eventNode.proceed(context);
        System.out.println("Proceeded: " + proceeded);

        ExamplePlugin.LOGGER.info(TSL.getJsEngine()
                .evaluate("'MyVariable: ' + (_getVariable('my_variable') + 1)", null));
    }

}
