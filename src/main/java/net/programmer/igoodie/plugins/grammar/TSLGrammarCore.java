package net.programmer.igoodie.plugins.grammar;

import net.programmer.igoodie.legacy.plugin.TSLPluginInstance;
import net.programmer.igoodie.plugins.grammar.actions.*;
import net.programmer.igoodie.plugins.grammar.comparators.EqualsComparator;
import net.programmer.igoodie.plugins.grammar.events.ManualTriggerEvent;
import net.programmer.igoodie.plugins.grammar.predicates.BinaryOperationPredicate;
import net.programmer.igoodie.plugins.grammar.predicates.BooleanPredicate;
import net.programmer.igoodie.plugins.grammar.tags.DebugTag;
import net.programmer.igoodie.plugins.grammar.tags.ImportTag;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.plugin.TSLCorePlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginDescriptor;
import net.programmer.igoodie.tsl.util.ValueHolder;
import org.pf4j.PluginDescriptor;

import java.util.List;

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

    @Override
    public List<ValueHolder<TSLEvent>> getEvents() {
        return createDefinitionList(
                ManualTriggerEvent.INSTANCE
        );
    }

    @Override
    public List<ValueHolder<TSLPredicate>> getPredicates() {
        return createDefinitionList(
                BooleanPredicate.INSTANCE,
                BinaryOperationPredicate.INSTANCE
        );
    }

    @Override
    public List<ValueHolder<TSLComparator>> getComparators() {
        return createDefinitionList(
                EqualsComparator.INSTANCE
        );
    }

}
