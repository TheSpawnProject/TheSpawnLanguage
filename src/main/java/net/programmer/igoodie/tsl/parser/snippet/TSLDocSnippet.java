package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;

import java.util.List;

// #**
//  * Some TSLDoc Here
//  * @param x Description of x
//  * @param y Description of y
//  *#
public class TSLDocSnippet extends TSLSnippet {

    TSLSymbol begin, end;
    List<TSLPlainWord> docTokens;

    public TSLDocSnippet(TSLSymbol begin, List<TSLPlainWord> docTokens, TSLSymbol end) {
        super(flatTokens(begin, docTokens, end));
        this.begin = begin;
        this.docTokens = docTokens;
        this.end = end;
    }

    public TSLSymbol getBegin() {
        return begin;
    }

    public List<TSLPlainWord> getDocTokens() {
        return docTokens;
    }

    public TSLSymbol getEnd() {
        return end;
    }

}
