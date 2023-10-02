package net.programmer.igoodie.legacy.parser.snippet;

import net.programmer.igoodie.legacy.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.definition.TSLEvent;

import java.util.List;
import java.util.stream.Collectors;

// [ON] [Event Name]
@Deprecated
public class TSLEventSnippet extends TSLSnippet {

    protected TSLPlainWord keywordOn;
    protected List<TSLPlainWord> eventName;

    protected TSLEvent eventDefinition;

    public TSLEventSnippet(TSLEvent eventDefinition, TSLPlainWord keywordOn, List<TSLPlainWord> eventName) {
        super(flatTokens(keywordOn, eventName));
        this.keywordOn = keywordOn;
        this.eventName = eventName;
        this.eventDefinition = eventDefinition;
    }

    public TSLPlainWord getOnKeywordToken() {
        return keywordOn;
    }

    public List<TSLPlainWord> getEventNameTokens() {
        return eventName;
    }

    /* ------------------------------ */

    public TSLEvent getEventDefinition() {
        return eventDefinition;
    }

    /* ------------------------------ */

    public String getEventName() {
        return eventName.stream()
                .map(TSLPlainWord::getRaw)
                .collect(Collectors.joining(" "));
    }

}
