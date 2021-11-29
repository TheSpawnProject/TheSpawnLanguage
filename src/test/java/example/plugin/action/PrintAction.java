package example.plugin.action;

import example.plugin.ExamplePlugin;
import example.plugin.decorator.SuppressNotificationsDecorator;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;

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
    public void validateTokens(List<TSLToken> arguments, TSLRule rule) throws TSLSyntaxError {
        // Should always be valid.
    }

    @Override
    public void perform(List<TSLToken> arguments, TSLContext context) {
        GoodieObject attributes = context.getAttributes();

        if(SuppressNotificationsDecorator.ATTR_NOTIFICATION_MUTED.isContained(attributes)) {
            ExamplePlugin.LOGGER.info("Suppressed.");
            return;
        }

        String printText = arguments.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.joining(" "));

        ExamplePlugin.LOGGER.info(printText + " @ " + attributes);
    }

}
