package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;

import java.util.List;

public class NothingMetaAction extends TSLAction {

    public static final NothingMetaAction INSTANCE = new NothingMetaAction();

    public NothingMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "NOTHING");
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public void validateTokens(TSLToken nameToken, List<TSLToken> arguments, TSLRule rule, TSLParser parser) throws TSLSyntaxError {
        if (arguments.size() != 0) {
            throw new TSLSyntaxError("NOTHING action does not accept any arguments", nameToken);
        }
    }

    @Override
    public void perform(List<TSLToken> arguments, TSLContext context) {
        // NOTHING action does nothing. Literally. What else did you expect? :P
    }

}
