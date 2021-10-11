package example.plugin;

import example.plugin.action.PrintAction;
import example.plugin.comparator.EqualsComparator;
import example.plugin.decorator.SuppressNotificationsDecorator;
import example.plugin.event.DummyEvent;
import example.plugin.fields.TimeField;
import example.plugin.functions.CurrentUnixFunction;
import example.plugin.functions.GetVariableFunction;
import example.plugin.functions.MaximumOfFunction;
import example.plugin.functions.MultFunction;
import example.plugin.tag.CooldownTag;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ExamplePlugin extends TSLPlugin {

    @TSLPluginInstance
    public static ExamplePlugin PLUGIN_INSTANCE;

    @TSLPluginLogger
    public static Logger LOGGER;

    public static final Map<String, Object> VARIABLE_CACHE = new HashMap<>();

    public ExamplePlugin() {
        super(new TSLPluginManifest("exampleplugin", "ExamplePlugin", "0.0.1"));
        getManifest().setAuthor("iGoodie");
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
    public void registerEventFields(TSLRegistry<TSLEventField<?>> registry) {
        registry.register(TimeField.INSTANCE);
    }

    @Override
    public void registerComparator(TSLRegistry<TSLComparator> registry) {
        registry.register(EqualsComparator.INSTANCE);
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
