package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;

import java.util.List;

// <Action> <Event> [Predicates]
public class TSLRuleSnippet extends TSLSnippet {

    protected List<TSLDecoratorCall> decorators;
    protected TSLActionSnippet actionSnippet;
    protected TSLEventSnippet eventSnippet;
    protected List<TSLPredicateSnippet> predicateSnippets;

    public TSLRuleSnippet(List<TSLDecoratorCall> decorators, TSLActionSnippet actionSnippet, TSLEventSnippet eventSnippet, List<TSLPredicateSnippet> predicateSnippets) {
        super(flatTokens(
                decorators,
                actionSnippet.getAllTokens(),
                eventSnippet.getAllTokens(),
                predicateSnippets));

        this.decorators = decorators;
        this.actionSnippet = actionSnippet;
        this.eventSnippet = eventSnippet;
        this.predicateSnippets = predicateSnippets;
    }

    public TSLActionSnippet getActionSnippet() {
        return actionSnippet;
    }

    public TSLEventSnippet getEventSnippet() {
        return eventSnippet;
    }

    public List<TSLPredicateSnippet> getPredicateSnippets() {
        return predicateSnippets;
    }

}
