package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableAction extends TSLAction {

    public static final VariableAction INSTANCE = new VariableAction();

    public final Map<String, Object> VARIABLE_CACHE = new HashMap<>();

    private VariableAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "VARIABLE");
    }

    @Override
    public String getUsage() {
        return getName() + " <var_name> <var_value>";
    }

    @Override
    public void validateTokens(List<TSLToken> arguments, TSLRule rule, TSLParser parser) throws TSLSyntaxError {
        if (arguments.size() != 2) {
            throw new TSLSyntaxError(getName() + " MUST have a variable name and a value", rule);
        }
    }

    @Override
    public void perform(List<TSLToken> arguments, TSLContext context) {
        String variableName = arguments.get(0).evaluate(context);
        String valueRaw = arguments.get(1).evaluate(context);

        try {
            double variableValue = Double.parseDouble(valueRaw);
            saveVariable(variableName, variableValue);
            return;
        } catch (NumberFormatException ignored) {}

        saveVariable(variableName, valueRaw);
    }

    public Object fetchVariable(String key) {
        return VARIABLE_CACHE.get(key);
    }

    public void saveVariable(String key, Object value) {
        VARIABLE_CACHE.put(key, value);
    }

}
