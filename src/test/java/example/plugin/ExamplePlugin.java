package example.plugin;

import example.plugin.action.PrintAction;
import example.plugin.decorator.SuppressNotificationsDecorator;
import example.plugin.event.DummyEvent;
import example.plugin.functions.CurrentUnixFunction;
import example.plugin.functions.GetVariableFunction;
import example.plugin.functions.MaximumOfFunction;
import example.plugin.functions.MultFunction;
import example.plugin.tag.CooldownTag;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.logging.Logger;

public class ExamplePlugin extends TSLPlugin {

    @TSLPluginInstance
    public static ExamplePlugin PLUGIN_INSTANCE;

    @TSLPluginLogger
    public static Logger LOGGER;

    public static final Map<String, Object> VARIABLE_CACHE = new HashMap<>();

    public ExamplePlugin() {
        try {
            Field manifestField = TSLPlugin.class.getDeclaredField("manifest");
            TSLPluginManifest manifest = new TSLPluginManifest(getJarAttributes());
            ReflectionUtilities.setValue(this, manifestField, manifest);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new InternalError();
        }
    }

    private Attributes getJarAttributes() {
        Attributes attributes = new Attributes();
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_ID, "exampleplugin");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_NAME, "Example Plugin");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_VERSION, "0.0.1");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_AUTHOR, "iGoodie");
        return attributes;
    }

    @Override
    public void registerTags(TSLRegistry<TSLTag> registry) {
        registry.register(CooldownTag.INSTANCE);
    }

    @Override
    public void registerDecorators(TSLRegistry<TSLDecorator> registry) {
        registry.register(SuppressNotificationsDecorator.INSTANCE);
    }

    @Override
    public void registerEvents(TSLRegistry<TSLEvent> registry) {
        registry.register(DummyEvent.INSTANCE);
        LOGGER.info("Registered all the events!");
    }

    @Override
    public void registerActions(TSLRegistry<TSLAction> registry) {
        registry.register(PrintAction.INSTANCE);
    }

    @Override
    public void registerFunctions(TSLRegistry<TSLFunction> registry) {
        registry.register(CurrentUnixFunction.INSTANCE);
        registry.register(MaximumOfFunction.INSTANCE);
        registry.register(GetVariableFunction.INSTANCE);
        registry.register(MultFunction.INSTANCE);
    }

}
