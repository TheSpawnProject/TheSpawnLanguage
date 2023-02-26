package example.plugin;

import net.programmer.igoodie.tsl.logging.TSLLogger;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.annotation.TSLPluginLogger;
import net.programmer.igoodie.tsl.plugin.manager.TSLPluginContext;

import java.util.HashMap;
import java.util.Map;

public class ExamplePlugin extends TSLPlugin {

    @TSLPluginInstance
    public static ExamplePlugin PLUGIN_INSTANCE;

    @TSLPluginLogger
    public static TSLLogger LOGGER;

    public static final Map<String, Object> VARIABLE_CACHE = new HashMap<>();

    protected ExamplePlugin(TSLPluginContext pluginContext) {
        super(pluginContext);
    }

//    public ExamplePlugin() {
//        super(new TSLPluginManifest(
//                "exampleplugin",
//                "Example Plugin",
//                "0.0.1",
//                "iGoodie"
//        ));
//    }

//    @Override
//    public void registerTags(TSLRegistry<TSLTag> registry) {
//        registry.register(CooldownTag.INSTANCE);
//    }
//
//    @Override
//    public void registerDecorators(TSLRegistry<TSLDecorator> registry) {
//        registry.register(SuppressNotificationsDecorator.INSTANCE);
//        registry.register(CooldownDecorator.INSTANCE);
//    }
//
//    @Override
//    public void registerEvents(TSLRegistry<TSLEvent> registry) {
//        registry.register(DummyEvent.INSTANCE);
//        LOGGER.info("Registered all the events!");
//    }
//
//    @Override
//    public void registerActions(TSLRegistry<TSLAction> registry) {
//        registry.register(PrintAction.INSTANCE);
//    }
//
//    @Override
//    public void registerFunctionLibraries(TSLRegistry<TSLFunctionLibrary> registry) {
//        registry.register(RootLibrary.INSTANCE);
//    }

}
