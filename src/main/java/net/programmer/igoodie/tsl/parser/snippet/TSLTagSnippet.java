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

    protected TSLTag tag;
    protected TSLString tagName;
    protected List<TSLString> tagArguments;

    public TSLTagSnippet(TSLRuleset ruleset, TSLTag tag, TSLSymbol tagSymbol, TSLString tagName, List<TSLString> tagArguments) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, tagSymbol, tagName, tagArguments));
        this.tag = tag;
        this.tagName = tagName;
        this.tagArguments = tagArguments;
    }

    public TSLTag getTag() {
        return tag;
    }

    public TSLString getTagName() {
        return tagName;
    }

    public List<TSLString> getTagArguments() {
        return tagArguments;
    }

}
