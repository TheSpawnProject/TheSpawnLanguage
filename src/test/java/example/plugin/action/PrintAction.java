package example.plugin.action;

import example.plugin.ExampleAttributes;
import example.plugin.ExamplePlugin;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.legacy.parser.TSLParserOld;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.legacy.runtime.TSLRuleOld;

import java.util.List;
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
    public void validateTokens(TSLToken nameToken, List<TSLToken> arguments, TSLRuleOld rule, TSLParserOld parser) throws TSLSyntaxError {
        // Should always be valid.
    }

    @Override
    public void perform(List<TSLToken> arguments, TSLContext context) {
        GoodieObject attributes = context.getAttributes();

        if (ExampleAttributes.NOTIFICATIONS_SUPPRESSED.isContained(attributes)) {
            ExamplePlugin.LOGGER.info("Suppressed.");
            return;
        }

        String printText = arguments.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.joining(" "));

        ExamplePlugin.LOGGER.info(printText + " @ " + attributes);
    }

}
