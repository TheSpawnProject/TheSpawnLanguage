package example.plugin;

import example.plugin.action.PrintAction;
import example.plugin.decorator.CooldownDecorator;
import example.plugin.decorator.SuppressNotificationsDecorator;
import example.plugin.event.DummyEvent;
import example.plugin.functions.RootLibrary;
import example.plugin.tag.CooldownTag;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.TSLTag;
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
        super(new TSLPluginManifest(
                "exampleplugin",
                "Example Plugin",
                "0.0.1",
                "iGoodie"
        ));
    }

    @Override
    public void registerTags(TSLRegistry<TSLTag> registry) {
        registry.register(CooldownTag.INSTANCE);
    }

    @Override
    public void registerDecorators(TSLRegistry<TSLDecorator> registry) {
        registry.register(SuppressNotificationsDecorator.INSTANCE);
        registry.register(CooldownDecorator.INSTANCE);
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
    public void registerFunctionLibraries(TSLRegistry<TSLFunctionLibrary> registry) {
        registry.register(RootLibrary.INSTANCE);
    }

}
