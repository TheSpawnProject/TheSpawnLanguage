package example.setup.action;

import example.setup.ExamplePlugin;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;
import java.util.stream.Collectors;

public class PrintAction extends TSLAction {

    public static final PrintAction INSTANCE = new PrintAction();

    private PrintAction() {
        super("PRINT");
    }

    @Override
    public void perform(List<TSLToken> tokens, TSLContext context) {
        String printText = tokens.stream().map(token -> token.evaluate(context))
                .collect(Collectors.joining(" "));

        ExamplePlugin.LOGGER.info(printText);
    }

}
