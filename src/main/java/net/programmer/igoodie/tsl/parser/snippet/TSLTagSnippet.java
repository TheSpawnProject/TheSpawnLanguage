package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

// [#!] [COOLDOWN] [3000]
public class TSLTagSnippet extends TSLSnippet {

    protected TSLSymbol tagSymbol;
    protected TSLPlainWord tagName;
    protected List<TSLToken> tagArguments;

    protected TSLTag tagDefinition;

    public TSLTagSnippet(TSLTag tagDefinition, TSLSymbol tagSymbol, TSLPlainWord tagName, List<TSLToken> tagArguments) {
        super(flatTokens(tagSymbol, tagName, tagArguments));
        this.tagSymbol = tagSymbol;
        this.tagName = tagName;
        this.tagArguments = tagArguments;
        this.tagDefinition = tagDefinition;
    }

    public TSLSymbol getTagSymbolToken() {
        return tagSymbol;
    }

    public TSLPlainWord getTagNameToken() {
        return tagName;
    }

    public List<TSLToken> getTagArgTokens() {
        return tagArguments;
    }

    /* ------------------------- */

    public TSLTag getTagDefinition() {
        return tagDefinition;
    }

}
