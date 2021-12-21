package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;

import java.util.List;

// #**
//  * Some TSLDoc Here
//  * @param x Description of x
//  * @param y Description of y
//  *#
public class TSLDocSnippet extends TSLSnippet {

    TSLSymbol begin, end;
    List<TSLString> docTokens;

    public TSLDocSnippet(TSLRuleset ruleset, TSLSymbol begin, List<TSLString> docTokens, TSLSymbol end) {
        super(ruleset, flatTokens(begin, docTokens, end));
        this.begin = begin;
        this.docTokens = docTokens;
        this.end = end;
    }

    public TSLSymbol getBegin() {
        return begin;
    }

    public List<TSLString> getDocTokens() {
        return docTokens;
    }

    public TSLSymbol getEnd() {
        return end;
    }

}
