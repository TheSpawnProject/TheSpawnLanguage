package example.plugin.action;

import example.plugin.ExampleAttributes;
import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParsingContext;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.stream.Collectors;

public class PrintAction extends TSLAction {

    public static final PrintAction INSTANCE = new PrintAction();

    private PrintAction() {
        super(ExamplePlugin.PLUGIN_INSTANCE, "PRINT");
    }

    @Override
    public String getUsage() {
        return getName() + " <text_to_print>";
    }

    @Override
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        // Should always be valid.
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        GoodieObject attributes = context.getAttributes();

        if (ExampleAttributes.NOTIFICATIONS_SUPPRESSED.isContained(attributes)) {
            ExamplePlugin.LOGGER.info("Suppressed.");
            return;
        }

        String printText = arguments.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.joining(" "));

        ExamplePlugin.LOGGER.info("PRINT -> " + printText + " @ " + attributes);
    }

}
