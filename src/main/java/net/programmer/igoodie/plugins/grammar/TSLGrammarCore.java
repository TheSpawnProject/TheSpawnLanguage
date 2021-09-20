package net.programmer.igoodie.plugins.grammar;

import net.programmer.igoodie.plugins.grammar.actions.VariableAction;
import net.programmer.igoodie.plugins.grammar.functions.RunScriptFunction;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.plugin.TSLPluginInstance;
import net.programmer.igoodie.tsl.plugin.TSLPluginManifest;
import net.programmer.igoodie.tsl.registry.ActionRegistry;
import net.programmer.igoodie.tsl.registry.FunctionRegistry;

import java.util.HashMap;
import java.util.Map;

public class TSLGrammarCore extends TSLPlugin {

    private static String VERSION = "2.0.0-alpha";

    @TSLPluginInstance
    public static TSLGrammarCore PLUGIN_INSTANCE;

    public static Map<String, Object> VARIABLE_CACHE = new HashMap<>();

    public TSLGrammarCore() {
        super(new TSLPluginManifest("core", "TSL Core", VERSION).setAuthor("iGoodie"));
    }

    @Override
    public void registerActions(ActionRegistry registry) {
        registry.register(VariableAction.INSTANCE);
    }

    @Override
    public void registerFunctions(FunctionRegistry registry) {
        registry.register(RunScriptFunction.INSTANCE);
    }

}
