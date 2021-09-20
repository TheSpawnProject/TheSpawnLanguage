package example.plugin.action;

import example.plugin.ExamplePlugin;
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
    public void verifyTokenCount(List<TSLToken> tokens, TSLRule rule) throws TSLSyntaxError {
        // TODO
    }

    @Override
    public void perform(List<TSLToken> tokens, TSLContext context) {
        GoodieObject attributes = context.getAttributes();

        if (attributes.containsKey("exampleplugin:notificationsMuted")) {
            ExamplePlugin.LOGGER.info("Suppressed.");
            return;
        }

        String printText = tokens.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.joining(" "));

        ExamplePlugin.LOGGER.info(printText);
    }

}
