package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.legacy.parser.TSLParsingContext;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;

public class NothingMetaAction extends TSLAction {

    public static final NothingMetaAction INSTANCE = new NothingMetaAction();

    private NothingMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "NOTHING");
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        if (arguments.size() != 0) {
            throw new TSLSyntaxError("NOTHING action does not accept any arguments", nameToken);
        }
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        // NOTHING action does nothing. Literally. What else did you expect? :P
    }

}
