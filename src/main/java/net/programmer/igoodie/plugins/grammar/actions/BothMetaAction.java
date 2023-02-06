package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParsingContext;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BothMetaAction extends TSLAction {

    public BothMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "BOTH");
    }

    @Override
    public String getUsage() {
        return getName() + " <action> [AND <action>]* [ALL DISPLAYING <message>]";
    }

    @Override
    public @NotNull Couple<List<TSLToken>, TSLToken> splitByDisplaying(List<TSLToken> tokens) {
        // TODO: ALL DISPLAYING cases
        return super.splitByDisplaying(tokens);
    }

    @Override
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        // TODO
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        // TODO
    }

}
