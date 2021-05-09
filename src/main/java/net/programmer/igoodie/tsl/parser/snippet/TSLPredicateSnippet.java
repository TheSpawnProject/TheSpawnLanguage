package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.List;

// [WITH] [field = value]
// [WITH] [true]
public class TSLPredicateSnippet extends TSLSnippet {

    protected List<TSLToken> tokens;

    public TSLPredicateSnippet(TSLRuleset ruleset, TSLString keywordWith, List<TSLToken> tokens) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, keywordWith, tokens));
        this.tokens = tokens;
    }

    public List<TSLToken> getTokens() {
        return tokens;
    }

}
