package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.attribute.TSLDecorator;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.snippet.*;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TSLParser {

    private TheSpawnLanguage tsl;

    public TSLParser(TheSpawnLanguage tsl) {
        this.tsl = tsl;
    }

    public TSLRuleset parse(String script) throws TSLSyntaxError {
        TSLRuleset ruleset = new TSLRuleset();

        // Foo

        TSLLexer lexer = new TSLLexer(script);
        lexer.lex();

        for (TSLSnippetBuffer buffer : lexer.getSnippetsBuffers()) {
            if (buffer.getType() == TSLSnippetBuffer.Type.TAG) {
                ruleset.addTag(parseTag(ruleset, buffer));

            } else if (buffer.getType() == TSLSnippetBuffer.Type.CAPTURE) {
                ruleset.addCapture(parseCapture(ruleset, buffer));

            } else if (buffer.getType() == TSLSnippetBuffer.Type.RULE) {
                TSLRule rule = parseRule(ruleset, buffer);
                TSLRuleSnippet ruleSnippet = rule.getSnippet();
                System.out.println("Decorators: " + rule.getAttributeList().getDecorators());
                System.out.println("Action: " + ruleSnippet.getActionSnippet());
                System.out.println("Event: " + ruleSnippet.getEventSnippet());
                System.out.println("Predicates: " + ruleSnippet.getPredicateSnippets());
                System.out.println("--------------------");
                // TODO: Add to ruleset
            }
        }

        return ruleset;
    }

    /* --------------------------- */

    public TSLTagSnippet parseTag(TSLRuleset ruleset, TSLSnippetBuffer buffer) throws TSLSyntaxError {
        List<TSLToken> tokens = buffer.getTokens();

        if (tokens.size() < 2) {
            throw new TSLSyntaxError("Malformed tag snippet", buffer);
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
            throw new TSLSyntaxError("Rules MUST have an Event statement.", buffer);
        }

        // Fetch index of first WITH keyword
        int indexWith = indexOfKeyword(tokens, "WITH");
        if (indexWith != -1 && indexOn > indexWith) {
            TSLToken keywordWith = tokens.get(indexWith);
            throw new TSLSyntaxError("Predicate statements MUST be declared after Event statement.", keywordWith);
        }

        // Extract event tokens and fetch event
        List<TSLString> eventTokens = getEventTokens(tokens, indexOn, indexWith);
        TSLEvent eventDefinition = getEvent(eventTokens);

        TSLEventSnippet eventSnippet = new TSLEventSnippet(ruleset,
                ((TSLString) tokens.get(indexOn)),
                eventTokens);

        // TODO: Parse predicates
        // TODO: Parse action

        // TODO: Compose Rule (as snippet)
        TSLRuleSnippet ruleSnippet = new TSLRuleSnippet(ruleset,
                decoratorCalls,
                new TSLActionSnippet(ruleset, CollectionUtils.asSpreadList(TSLToken.class, new TSLString(0, 0, "TODO"))),
                eventSnippet,
                new LinkedList<>());

        rule.setSnippet(ruleSnippet);

        return rule;
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

    private List<TSLString> getEventTokens(List<TSLToken> tokens, int indexOn, int indexWith) {
        List<TSLToken> eventTokens = tokens.subList(indexOn + 1,
                indexWith == -1 ? tokens.size() : indexWith);

        if (eventTokens.size() == 0) {
            TSLToken keywordOn = tokens.get(indexOn);
            throw new TSLSyntaxError("Event statement MUST contain at least one word", keywordOn);
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

    private TSLEvent getEvent(List<TSLString> eventTokens) {
        String eventName = eventTokens.stream()
                .map(TSLString::getRaw)
                .collect(Collectors.joining(" "));
        TSLEvent tslEvent = tsl.EVENT_REGISTRY.get(eventName);

        if (tslEvent == null) {
            throw new TSLSyntaxError("Unknown event statement -> " + eventName, eventTokens.get(0));
        }

        return tslEvent;
    }

}
