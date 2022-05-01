package net.programmer.igoodie.plugins.grammar;

import net.programmer.igoodie.plugins.grammar.actions.*;
import net.programmer.igoodie.plugins.grammar.comparators.EqualsComparator;
import net.programmer.igoodie.plugins.grammar.events.ManualTriggerEvent;
import net.programmer.igoodie.plugins.grammar.predicates.BinaryOperationPredicate;
import net.programmer.igoodie.plugins.grammar.predicates.BooleanPredicate;
import net.programmer.igoodie.plugins.grammar.tags.DebugTag;
import net.programmer.igoodie.plugins.grammar.tags.ImportTag;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;

public class TSLGrammarCore extends TSLPlugin {

    private static final String VERSION = "2.0.0-alpha";

    @TSLPluginInstance
    public static TSLGrammarCore PLUGIN_INSTANCE;

    public TSLGrammarCore() {
        super(new TSLPluginManifest(
                "tsl_core",
                "TSL Core",
                VERSION,
                "iGoodie"
        ));
    }

    @Override
    public void registerTags(TSLRegistry<TSLTag> registry) {
        registry.register(DebugTag.INSTANCE);
        registry.register(ImportTag.INSTANCE);
    }

    @Override
    public void registerActions(TSLRegistry<TSLAction> registry) {
        registry.register(IfMetaAction.INSTANCE);
        registry.register(ForMetaAction.INSTANCE);
        registry.register(WaitMetaAction.INSTANCE);
        registry.register(NothingMetaAction.INSTANCE);
        registry.register(VariableAction.INSTANCE);
    }

    @Override
    public void registerEvents(TSLRegistry<TSLEvent> registry) {
        registry.register(ManualTriggerEvent.INSTANCE);
    }

    @Override
    public void registerPredicates(TSLRegistry<TSLPredicate> registry) {
        registry.register(BooleanPredicate.INSTANCE);
        registry.register(BinaryOperationPredicate.INSTANCE);
    }

    @Override
    public void registerComparators(TSLRegistry<TSLComparator> registry) {
        registry.register(EqualsComparator.INSTANCE);
    }

}
