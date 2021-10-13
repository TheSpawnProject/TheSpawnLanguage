package net.programmer.igoodie.plugins.grammar.predicates;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

public class BooleanPredicate extends TSLPredicate {

    public static final BooleanPredicate INSTANCE = new BooleanPredicate();

    private BooleanPredicate() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "Boolean");
    }

    @Override
    public boolean formatMatches(List<TSLToken> tokens) {
        return tokens.size() == 1;
    }

    @Override
    public boolean satisfies(TSLContext context, List<TSLToken> tokens) {
        TSLToken token = tokens.get(0);
        String evaluation = token.evaluate(context);
        return evaluation.equalsIgnoreCase("true");
    }

}
