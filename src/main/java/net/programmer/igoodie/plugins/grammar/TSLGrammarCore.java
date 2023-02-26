package net.programmer.igoodie.plugins.grammar;

import net.programmer.igoodie.legacy.plugin.TSLPluginInstance;
import net.programmer.igoodie.plugins.grammar.actions.*;
import net.programmer.igoodie.plugins.grammar.tags.DebugTag;
import net.programmer.igoodie.plugins.grammar.tags.ImportTag;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.plugin.TSLCorePlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginDescriptor;
import net.programmer.igoodie.tsl.util.ValueHolder;
import org.pf4j.Extension;
import org.pf4j.PluginDescriptor;

import java.util.List;

@Extension
public class TSLGrammarCore extends TSLCorePlugin {

    public static final TSLPluginDescriptor DESCRIPTOR = createCoreDescriptor(
            "tsl_core",
            "TSL Core",
            "TODO: Description here",
            "2.0.0-alpha",
            "iGoodie",
            "MIT"
    );

    @TSLPluginInstance
    public static TSLGrammarCore PLUGIN_INSTANCE;

    @Override
    public PluginDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public List<ValueHolder<TSLTag>> getTags() {
        return createDefinitionList(
                DebugTag.INSTANCE,
                ImportTag.INSTANCE
        );
    }

    @Override
    public List<ValueHolder<TSLAction>> getActions() {
        return createDefinitionList(
                IfMetaAction.INSTANCE,
                ForMetaAction.INSTANCE,
                WaitMetaAction.INSTANCE,
                NothingMetaAction.INSTANCE,
                DoMetaAction.INSTANCE,
                // TODO: BOTH Meta-action
                // TODO: EITHER Meta-action
                VariableAction.INSTANCE
        );
    }

//    @Override
//    public void registerEvents(TSLRegistry<TSLEvent> registry) {
//        registry.register(ManualTriggerEvent.INSTANCE);
//    }
//
//    @Override
//    public void registerPredicates(TSLRegistry<TSLPredicate> registry) {
//        registry.register(BooleanPredicate.INSTANCE);
//        registry.register(BinaryOperationPredicate.INSTANCE);
//    }
//
//    @Override
//    public void registerComparators(TSLRegistry<TSLComparator> registry) {
//        registry.register(EqualsComparator.INSTANCE);
//    }

}
