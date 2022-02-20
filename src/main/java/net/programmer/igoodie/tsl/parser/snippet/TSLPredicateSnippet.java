package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.List;

// [WITH] [field = value]
// [WITH] [true]
public class TSLPredicateSnippet extends TSLSnippet {

    protected TSLPlainWord keywordWith;
    protected List<TSLToken> predicateTokens;

    protected TSLPredicate predicateDefinition;

    public TSLPredicateSnippet(TSLRuleset ruleset, TSLPredicate predicateDefinition, TSLPlainWord keywordWith, List<TSLToken> tokens) {
        super(ruleset, flatTokens(keywordWith, tokens));
        this.keywordWith = keywordWith;
        this.predicateTokens = tokens;
        this.predicateDefinition = predicateDefinition;
    }

    public TSLPlainWord getWithKeywordToken() {
        return keywordWith;
    }

    public List<TSLToken> getPredicateTokens() {
        return predicateTokens;
    }

    /* ---------------------------------- */

    public TSLPredicate getPredicateDefinition() {
        return predicateDefinition;
    }

}
