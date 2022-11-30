package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParsingContext;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;

public class DoMetaAction extends TSLAction {

    public static final DoMetaAction INSTANCE = new DoMetaAction();

    private DoMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "DO");
    }

    @Override
    public String getUsage() {
        return getName() + " <${actionToken}>";
    }

    @Override
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        // TODO: Should have exactly 1 token
        // TODO: Should be an expression token
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        arguments.get(0).ifPresent(token -> token.evaluate(context));
    }

}
