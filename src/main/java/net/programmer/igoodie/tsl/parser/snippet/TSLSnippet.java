package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.List;

public abstract class TSLSnippet {

    protected TSLRuleset ruleset;
    protected List<TSLToken> allTokens;

    public TSLSnippet(TSLRuleset ruleset, List<TSLToken> allTokens) {
        if (allTokens.size() <= 0) {
            throw new InternalError("A Snippet MUST have at least one token.");
        }

        this.ruleset = ruleset;
        this.allTokens = allTokens;
    }

    public TSLRuleset getRuleset() {
        return ruleset;
    }

    public List<TSLToken> getAllTokens() {
        return allTokens;
    }

}
