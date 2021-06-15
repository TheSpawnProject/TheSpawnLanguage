package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

// [ON] [Event Name]
public class TSLEventSnippet extends TSLSnippet {

    protected List<TSLString> eventName;
    protected TSLEvent eventDefinition;

    public TSLEventSnippet(TSLRuleset ruleset, TSLEvent eventDefinition, TSLString keywordOn, List<TSLString> eventName) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, keywordOn, eventName));
        this.eventName = eventName;
        this.eventDefinition = eventDefinition;
    }

    public TSLEvent getEventDefinition() {
        return eventDefinition;
    }

    public List<TSLString> getEventNameWords() {
        return eventName;
    }

    public String getEventName() {
        return eventName.stream()
                .map(TSLString::getRaw)
                .collect(Collectors.joining(" "));
    }

}
