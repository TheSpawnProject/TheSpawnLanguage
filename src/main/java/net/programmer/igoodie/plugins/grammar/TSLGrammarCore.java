package net.programmer.igoodie.plugins.grammar;

import net.programmer.igoodie.plugins.grammar.actions.*;
import net.programmer.igoodie.plugins.grammar.comparators.EqualsComparator;
import net.programmer.igoodie.plugins.grammar.functions.RunScriptFunction;
import net.programmer.igoodie.plugins.grammar.predicates.BinaryOperationPredicate;
import net.programmer.igoodie.plugins.grammar.predicates.BooleanPredicate;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.TSLRegistry;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.jar.Attributes;

public class TSLGrammarCore extends TSLPlugin {

    private static final String VERSION = "2.0.0-alpha";

    @TSLPluginInstance
    public static TSLGrammarCore PLUGIN_INSTANCE;

    public TSLGrammarCore() {
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
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_ID, "tsl_core");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_NAME, "TSL Core");
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_VERSION, VERSION);
        attributes.putValue(TSLPluginManifest.ATTR_PLUGIN_AUTHOR, "iGoodie");
        return attributes;
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
    public void registerFunctions(TSLRegistry<TSLFunction> registry) {
        registry.register(RunScriptFunction.INSTANCE);
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
