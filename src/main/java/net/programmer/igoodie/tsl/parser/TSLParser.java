package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.*;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import net.programmer.igoodie.util.Couple;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TSLParser {

    private final TheSpawnLanguage tsl;

    public TSLParser(TSLContext context) {
        this(context.getLanguage());
    }

    public TSLParser(TheSpawnLanguage tsl) {
        this.tsl = tsl;
    }

    public TSLRuleset parse(String script) throws TSLSyntaxError {
        TSLRuleset ruleset = new TSLRuleset();

        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        for (TSLSnippetBuffer buffer : lexer.getSnippets()) {
            if (buffer.getType() == TSLSnippetBuffer.Type.TAG) {
                ruleset.addTag(parseTag(ruleset, buffer));

            } else if (buffer.getType() == TSLSnippetBuffer.Type.CAPTURE) {
                ruleset.addCapture(parseCapture(ruleset, buffer));

            } else if (buffer.getType() == TSLSnippetBuffer.Type.RULE) {
                ruleset.addRule(parseRule(ruleset, buffer));
            }
        }

        return ruleset;
    }

    /* --------------------------- */

    public TSLTagSnippet parseTag(TSLRuleset ruleset, TSLSnippetBuffer buffer) throws TSLSyntaxError {
        List<TSLToken> tokens = buffer.getTokens();

        if (tokens.size() < 2) {
            throw new TSLSyntaxError("Tag snippet missing a tag name", buffer);
        }

        TSLToken tagNameToken = tokens.get(1);
        TSLTag tagDefinition = tsl.TAG_REGISTRY.get(tagNameToken.getRaw());

        if (tagDefinition == null) {
            throw new TSLSyntaxError(String.format("Unknown tag name -> %s", tagNameToken.getRaw()), buffer);
        }

        if (!(tagNameToken instanceof TSLString)) {
            throw new TSLSyntaxError(String.format("Invalid value -> %s", tagNameToken.getRaw()), buffer);
        }

        List<TSLToken> argTokens = tokens.subList(1, tokens.size());

        for (TSLToken argToken : argTokens) {
            if (!(argToken instanceof TSLString)) {
                throw new TSLSyntaxError(String.format("Invalid value -> %s", argToken.getRaw()), buffer);
            }
        }

        return new TSLTagSnippet(ruleset,
                tagDefinition,
                ((TSLSymbol) tokens.get(0)),
                ((TSLString) tagNameToken),
                tokens.subList(2, tokens.size()).stream()
                        .map(token -> ((TSLString) token))
                        .collect(Collectors.toList()));
    }

    public TSLCaptureSnippet parseCapture(TSLRuleset ruleset, TSLSnippetBuffer buffer) throws TSLSyntaxError {
        List<TSLToken> tokens = buffer.getTokens();

        if (tokens.size() < 3) {
            throw new TSLSyntaxError("Malformed capture snippet", buffer);
        }

        TSLToken captureNameToken = tokens.get(0);

        if (!(captureNameToken instanceof TSLCaptureCall)) {
            throw new TSLSyntaxError(String.format("Invalid capture header -> %s", captureNameToken.getRaw()), buffer);
        }

        TSLToken equalsSign = tokens.get(1);

        if (!(equalsSign instanceof TSLSymbol) || ((TSLSymbol) equalsSign).getType() != TSLSymbol.Type.CAPTURE_DECLARATION) {
            throw new TSLSyntaxError(String.format("Unexpected token -> %s", equalsSign.getRaw()), buffer);
        }
        List<TSLToken> capturedTokens = tokens.subList(2, tokens.size());

        return new TSLCaptureSnippet(ruleset,
                ((TSLCaptureCall) captureNameToken),
                ((TSLSymbol) equalsSign),
                capturedTokens);
    }

    public TSLRule parseRule(TSLRuleset ruleset, TSLSnippetBuffer buffer) throws TSLSyntaxError {
        TSLRule rule = new TSLRule();
        List<TSLToken> tokens = buffer.getTokens();

        // Fetch index of last Decorator call
        int indexLastDecorator = indexLastDecorator(tokens);

        // Parse and bind Decorators to the rule
        List<TSLDecoratorCall> decoratorCalls = parseDecorators(rule, tokens);

        // Fetch index of ON keyword
        int indexOn = indexOfKeyword(tokens, "ON");
        if (indexOn == -1) {
            // TODO: Can we fill captures before even checking on events?
            throw new TSLSyntaxError("Incomplete rule. Missing an event statement.", buffer);
        }

        // Fetch index of first WITH keyword
        int indexWith = indexOfKeyword(tokens, "WITH");
        if (indexWith != -1 && indexOn > indexWith) {
            TSLToken keywordWith = tokens.get(indexWith);
            throw new TSLSyntaxError("Predicate statements MUST be declared after Event statement.", keywordWith);
        }

        // Parse 3 fundamental parts
        TSLEventSnippet eventSnippet = parseEvent(ruleset, tokens, indexOn, indexWith);
        TSLActionSnippet actionSnippet = parseAction(ruleset, tokens, indexLastDecorator, indexOn);
        List<TSLPredicateSnippet> predicateSnippets = parsePredicates(ruleset, eventSnippet, tokens, indexWith);

        // Compose a rule snippet and bind
        TSLRuleSnippet ruleSnippet = new TSLRuleSnippet(ruleset,
                decoratorCalls,
                actionSnippet,
                eventSnippet,
                predicateSnippets);

        rule.setSnippet(ruleSnippet);

        // Destruct action tokens
        TSLString actionNameToken = actionSnippet.getActionNameToken();
        TSLAction actionDefinition = actionSnippet.getActionDefinition();

        Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(actionSnippet.getActionTokens());
        List<TSLToken> actionTokens = couple.getFirst();
        TSLToken messageToken = couple.getSecond();

        // Validate actions arguments
        actionDefinition.validateTokens(actionNameToken, actionTokens, rule, this);

        return rule;
    }

    public TSLEventSnippet parseEvent(TSLRuleset ruleset, List<TSLToken> tokens, int indexOn, int indexWith) {
        List<TSLString> eventTokens = getEventNameTokens(tokens, indexOn, indexWith);
        TSLEvent eventDefinition = getEvent(eventTokens);

        return new TSLEventSnippet(ruleset, eventDefinition,
                ((TSLString) tokens.get(indexOn)),
                eventTokens);
    }

    public TSLActionSnippet parseAction(TSLRuleset ruleset, List<TSLToken> tokens, int indexLastDecorator, int indexOn) {
        List<TSLToken> actionTokens = TSLActionSnippet.flatten(ruleset,
                tokens.subList(indexLastDecorator == -1 ? 0 : indexLastDecorator + 1, indexOn));

        if (actionTokens.size() == 0) {
            throw new TSLSyntaxError("Rule missing action tokens.", tokens.get(0));
        }
        return parseAction(ruleset, actionTokens);
    }

    public TSLActionSnippet parseAction(@Nullable TSLRuleset ruleset, List<TSLToken> tokens) {
        TSLToken actionName = tokens.get(0);
        List<TSLToken> actionTokens = tokens.subList(1, tokens.size());

        if (!(actionName instanceof TSLString)) {
            throw new TSLSyntaxError("Action name MUST be a String Word.", actionName);
        }

        TSLAction actionDefinition = getAction(((TSLString) actionName));

        Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(actionTokens);
        List<TSLToken> actionTokensSplitted = couple.getFirst();

        actionDefinition.validateTokens(actionName, actionTokensSplitted, null, this);

        return new TSLActionSnippet(ruleset, actionDefinition,
                ((TSLString) actionName), actionTokens);
    }

    public List<TSLPredicateSnippet> parsePredicates(TSLRuleset ruleset, TSLEventSnippet eventSnippet, List<TSLToken> tokens, int indexWith) {
        if (indexWith == -1) {
            return Collections.emptyList();
        }

        List<TSLPredicateSnippet> predicateSnippets = new LinkedList<>();

        List<TSLToken> predicatePart = tokens.subList(indexWith, tokens.size());
        List<TSLToken> predicateTokens = new LinkedList<>();

        for (int i = 0; i < predicatePart.size(); i++) {
            TSLToken token = predicatePart.get(i);
            boolean isLastToken = i == predicatePart.size() - 1;
            if (isLastToken || token instanceof TSLString && token.getRaw().equalsIgnoreCase("WITH")) {
                if (isLastToken) predicateTokens.add(token);
                if (!predicateTokens.isEmpty()) {
                    predicateSnippets.add(parsePredicate(ruleset, eventSnippet, predicateTokens));
                    predicateTokens = new LinkedList<>();
                }
            }
            predicateTokens.add(token);
        }

        return predicateSnippets;
    }

    public TSLPredicateSnippet parsePredicate(TSLRuleset ruleset, TSLEventSnippet eventSnippet, List<TSLToken> tokens) {
        TSLToken withToken = tokens.get(0);

        if (!(withToken instanceof TSLString)) {
            throw new TSLSyntaxError("Predicates MUST start with WITH keyword.", withToken);
        }

        if (!((TSLString) withToken).getWord().equalsIgnoreCase("WITH")) {
            throw new TSLSyntaxError("Predicates MUST start with WITH keyword.", withToken);
        }

        List<TSLToken> predicateTokens = tokens.subList(1, tokens.size());

        Optional<TSLPredicate> matchingPredicateFormat = tsl.PREDICATE_REGISTRY.stream()
                .map(Map.Entry::getValue)
                .filter(predicate -> predicate.formatMatches(tsl, eventSnippet.getEventDefinition(), predicateTokens))
                .findFirst();

        if (!matchingPredicateFormat.isPresent()) {
            throw new TSLSyntaxError("Unknown Predicate declaration format.", withToken);
        }

        return new TSLPredicateSnippet(ruleset,
                matchingPredicateFormat.get(),
                ((TSLString) withToken),
                predicateTokens);
    }

    public List<TSLDecoratorCall> parseDecorators(TSLRule rule, List<TSLToken> tokens) {
        int indexLastDecorator = indexLastDecorator(tokens);
        List<TSLDecoratorCall> decoratorTokens = tokens.subList(0,
                        indexLastDecorator == -1 ? 0 : indexLastDecorator + 1)
                .stream().map(token -> ((TSLDecoratorCall) token))
                .collect(Collectors.toList());

        for (TSLDecoratorCall decoratorToken : decoratorTokens) {
            TSLDecorator decorator = parseDecorator(decoratorToken);
            rule.addDecorator(decorator, decoratorToken);
        }

        return decoratorTokens;
    }

    public TSLDecorator parseDecorator(TSLDecoratorCall decoratorCall) {
        TSLDecorator decorator = tsl.DECORATOR_REGISTRY.get(decoratorCall.getName());

        if (decorator == null) {
            throw new TSLSyntaxError("Unknown decorator name -> " + decoratorCall.getName(), decoratorCall);
        }

        return decorator;
    }

    /* --------------------------- */

    private int indexOfKeyword(List<TSLToken> tokens, String keyword) {
        return CollectionUtils.indexOfBy(tokens,
                token -> token.getRaw().equalsIgnoreCase(keyword));
    }

    private int indexLastDecorator(List<TSLToken> tokens) {
        int lastIndex = -1;
        boolean decoratorAllowed = true;

        for (int i = 0; i < tokens.size(); i++) {
            TSLToken token = tokens.get(i);
            if (token instanceof TSLDecoratorCall) {
                lastIndex = i;
                if (!decoratorAllowed) {
                    throw new TSLSyntaxError("Decorators MUST be declared on top of the rules.", token);
                }
            } else {
                decoratorAllowed = false;
            }
        }

        return lastIndex;
    }

    /* --------------------------- */

    private TSLEvent getEvent(List<TSLString> eventTokens) {
        String eventName = eventTokens.stream()
                .map(TSLString::getRaw)
                .collect(Collectors.joining(" "));
        TSLEvent tslEvent = tsl.EVENT_REGISTRY.get(eventName);

        if (tslEvent == null) {
            throw new TSLSyntaxError("Unknown event name -> " + eventName, eventTokens.get(0));
        }

        return tslEvent;
    }

    private List<TSLString> getEventNameTokens(List<TSLToken> tokens, int indexOn, int indexWith) {
        List<TSLToken> eventTokens = tokens.subList(indexOn + 1,
                indexWith == -1 ? tokens.size() : indexWith);

        if (eventTokens.size() == 0) {
            TSLToken keywordOn = tokens.get(indexOn);
            throw new TSLSyntaxError("Missing event name.", keywordOn);
        }

        List<TSLString> eventTokensAsString = new LinkedList<>();
        for (TSLToken token : eventTokens) {
            if (!(token instanceof TSLString)) {
                throw new TSLSyntaxError("Event statements MUST contain only String Words. Instead found -> " + token.getTypeName() + " " + token.getRaw(), token);
            }
            eventTokensAsString.add(((TSLString) token));
        }

        return eventTokensAsString;
    }

    /* --------------------------- */

    private TSLAction getAction(TSLString actionName) {
        TSLAction tslAction = tsl.ACTION_REGISTRY.get(actionName.getRaw());

        if (tslAction == null) {
            throw new TSLSyntaxError("Unknown action -> " + actionName.getRaw(), actionName);
        }

        return tslAction;
    }

}
