package example.setup;

import example.setup.action.PrintAction;
import example.setup.action.VariableAction;
import example.setup.comparator.EqualsComparator;
import example.setup.decorator.SuppressNotificationsDecorator;
import example.setup.event.AlertEvent;
import example.setup.fields.TimeField;
import example.setup.functions.CurrentUnixFunction;
import example.setup.functions.GetVariableFunction;
import example.setup.functions.MaximumOfFunction;
import example.setup.functions.MultFunction;
import example.setup.tag.CooldownTag;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.*;

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
    public void registerTags(TagRegistry registry) {
        registry.register(CooldownTag.INSTANCE);
    }

    @Override
    public void registerDecorators(DecoratorRegistry registry) {
        registry.register(SuppressNotificationsDecorator.INSTANCE);
    }

    @Override
    public void registerEvents(EventRegistry registry) {
        registry.register(AlertEvent.INSTANCE);
        LOGGER.info("Registered all the events!");
    }

    @Override
    public void registerEventFields(EventFieldRegistry registry) {
        registry.register(TimeField.INSTANCE);
    }

    @Override
    public void registerComparator(ComparatorRegistry registry) {
        registry.register(EqualsComparator.INSTANCE);
    }

    @Override
    public void registerActions(ActionRegistry registry) {
        registry.register(PrintAction.INSTANCE);
        registry.register(VariableAction.INSTANCE);
    }

    @Override
    public void registerFunctions(FunctionRegistry registry) {
        registry.register(CurrentUnixFunction.INSTANCE);
        registry.register(MaximumOfFunction.INSTANCE);
        registry.register(GetVariableFunction.INSTANCE);
        registry.register(MultFunction.INSTANCE);
    }

}
