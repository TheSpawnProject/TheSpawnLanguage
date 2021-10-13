package net.programmer.igoodie.plugins.grammar.predicates;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

public class BinaryOperationPredicate extends TSLPredicate {

    public static final BinaryOperationPredicate INSTANCE = new BinaryOperationPredicate();

    private BinaryOperationPredicate() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "Binary Operation");
    }

    @Override
    public boolean formatMatches(List<TSLToken> tokens) {
        return false; // TODO
    }

    @Override
    public boolean satisfies(TSLContext context, List<TSLToken> tokens) {
        return false; // TODO
    }

}
