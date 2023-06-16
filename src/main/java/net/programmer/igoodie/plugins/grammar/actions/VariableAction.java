package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.legacy.parser.TSLParsingContext;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.HashMap;
import java.util.Map;

// TODO: Maybe turn into ${persist.myVariable = 999} ?
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
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        if (arguments.size() != 2) {
            throw new TSLSyntaxError(getName() + " MUST have a variable name and a value").at(nameToken);
        }
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        String variableName = arguments.get(0).map(token -> token.evaluate(context)).orElse("");
        String valueRaw = arguments.get(1).map(token -> token.evaluate(context)).orElse("");

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
