package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLSymbol;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.List;

// [#!] [COOLDOWN] [3000]
public class TSLTagSnippet extends TSLSnippet {

    protected TSLSymbol tagSymbol;
    protected TSLString tagName;
    protected List<TSLString> tagArguments;

    protected TSLTag tagDefinition;

    public TSLTagSnippet(TSLRuleset ruleset, TSLTag tagDefinition, TSLSymbol tagSymbol, TSLString tagName, List<TSLString> tagArguments) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, tagSymbol, tagName, tagArguments));
        this.tagSymbol = tagSymbol;
        this.tagName = tagName;
        this.tagArguments = tagArguments;
        this.tagDefinition = tagDefinition;
    }

    public TSLSymbol getTagSymbolToken() {
        return tagSymbol;
    }

    public TSLString getTagNameToken() {
        return tagName;
    }

    public List<TSLString> getTagArgTokens() {
        return tagArguments;
    }

    /* ------------------------- */

    public TSLTag getTagDefinition() {
        return tagDefinition;
    }

}
