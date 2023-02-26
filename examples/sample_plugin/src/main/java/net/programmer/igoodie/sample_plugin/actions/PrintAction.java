package net.programmer.igoodie.sample_plugin.actions;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.sample_plugin.SamplePlugin;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.parser.TSLParsingContext;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.stream.Collectors;

public class PrintAction extends TSLAction {

    public static final PrintAction INSTANCE = new PrintAction();

    private PrintAction() {
        super(SamplePlugin.PLUGIN_INSTANCE, "PRINT");
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

        String printText = arguments.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.joining(" "));

        SamplePlugin.LOGGER.info("PRINT -> {} @ {}", printText, attributes);
    }

}
