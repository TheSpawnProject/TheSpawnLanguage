package example.setup.action;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

public class VariableAction extends TSLAction {

    public static final VariableAction INSTANCE = new VariableAction();

    private VariableAction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "VARIABLE");
    }

    @Override
    public void perform(List<TSLToken> tokens, TSLContext context) {
        String variableName = tokens.get(0).evaluate(context);
        String valueRaw = tokens.get(1).evaluate(context);

        try {
            double value = Double.parseDouble(valueRaw);
            ExamplePlugin.VARIABLE_CACHE.put(variableName, value);
            return;
        } catch (NumberFormatException ignored) { }


        ExamplePlugin.VARIABLE_CACHE.put(variableName, valueRaw);
    }

}
