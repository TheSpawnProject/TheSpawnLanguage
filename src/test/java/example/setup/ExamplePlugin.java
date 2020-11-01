package example.setup;

import example.setup.comparator.EqualsComparator;
import example.setup.event.AlertEvent;
import example.setup.fields.TimeField;
import example.setup.functions.CurrentUnixFunction;
import example.setup.functions.MaximumOfFunction;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.ComparatorRegistry;
import net.programmer.igoodie.tsl.registry.EventFieldRegistry;
import net.programmer.igoodie.tsl.registry.EventRegistry;
import net.programmer.igoodie.tsl.registry.FunctionRegistry;

public class ExamplePlugin extends TSLPlugin {

    public ExamplePlugin() {
        super(new TSLPluginManifest("ExamplePlugin", "0.0.1"));
        getManifest().setAuthor("iGoodie");
    }

    @Override
    public void registerEvents(EventRegistry registry) {
        registry.register(AlertEvent.INSTANCE);
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
    public void registerFunctions(FunctionRegistry registry) {
        registry.register(CurrentUnixFunction.INSTANCE);
        registry.register(MaximumOfFunction.INSTANCE);
    }

}
