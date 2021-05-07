package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;

import java.util.List;

public class VariableAction extends TSLAction {

    public static final VariableAction INSTANCE = new VariableAction();

    private VariableAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "VARIABLE");
    }

    @Override
    public String getUsage() {
        return getName() + " <var_name> <var_value>";
    }

    @Override
    public void verifyTokenCount(List<TSLToken> tokens, TSLRule rule) throws TSLSyntaxError {
        if (tokens.size() != 2) {
            throw new TSLSyntaxError(getName() + " MUST have a variable name and a value", rule);
        }
    }

    @Override
    public void perform(List<TSLToken> tokens, TSLContext context) {
        String variableName = tokens.get(0).evaluate(context);
        String valueRaw = tokens.get(1).evaluate(context);

        try {
            double variableValue = Double.parseDouble(valueRaw);
            saveVariable(variableName, variableValue);
            return;
        } catch (NumberFormatException ignored) {}

        saveVariable(variableName, valueRaw);
    }

    public Object fetchVariable(String key) {
        return TSLGrammarCore.VARIABLE_CACHE.get(key);
    }

    public void saveVariable(String key, Object value) {
        TSLGrammarCore.VARIABLE_CACHE.put(key, value);
    }

}
