package net.programmer.igoodie.legacy.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.legacy.parser.token.TSLPlainWord;
import net.programmer.igoodie.legacy.parser.token.TSLToken;

import java.util.List;

// [WITH] [field = value]
// [WITH] [true]
public class TSLPredicateSnippet extends TSLSnippet {

    protected TSLPlainWord keywordWith;
    protected List<TSLToken> predicateTokens;

    protected TSLPredicate predicateDefinition;

    public TSLPredicateSnippet(TSLPredicate predicateDefinition, TSLPlainWord keywordWith, List<TSLToken> tokens) {
        super(flatTokens(keywordWith, tokens));
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
