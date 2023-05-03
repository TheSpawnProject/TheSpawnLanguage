package net.programmer.igoodie.legacy.parser;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.legacy.parser.lexer.TSLLexer;
import net.programmer.igoodie.legacy.parser.snippet.*;
import net.programmer.igoodie.legacy.parser.token.*;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.*;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import net.programmer.igoodie.tsl.util.IOUtils;
import net.programmer.igoodie.tsl.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TSLParser {

    private final TheSpawnLanguage tsl;
    private TSLRuleset ruleset;

    public TSLParser(TSLContext context) {
        this(context.getTsl());
    }

    public TSLParser(TheSpawnLanguage tsl) {
        this.tsl = tsl;
        this.ruleset = new TSLRuleset(tsl);
    }

    public TheSpawnLanguage getTsl() {
        return tsl;
    }

    public TSLRuleset parse(URL url) throws TSLSyntaxError, URISyntaxException {
        return parse(new File(url.toURI()));
    }

    public TSLRuleset parse(File file) throws TSLSyntaxError {
        ruleset = new TSLRuleset(tsl, file.getName(), file);
        return parse(IOUtils.readString(file));
    }

    public TSLRuleset parse(String script) throws TSLSyntaxError {
        TSLLexer lexer = new TSLLexer(script).lex();

        System.out.println(lexer.getSnippets());

        for (TSLTokenBuffer buffer : lexer.getSnippets()) {
            if (buffer.getType() == TSLTokenBuffer.Type.TAG) {
                ruleset.addTag(parseTag(ruleset.getImportedPlugins(), buffer));

            } else if (buffer.getType() == TSLTokenBuffer.Type.CAPTURE) {
                ruleset.addCapture(parseCapture(buffer));

            } else if (buffer.getType() == TSLTokenBuffer.Type.RULE) {
                ruleset.addRule(parseRule(ruleset.getImportedPlugins(), ruleset.getCaptureSnippets(), buffer));

            } else if (buffer.getType() == TSLTokenBuffer.Type.TSLDOC) {
                ruleset.addTSLDoc(parseTSLDoc(buffer));
            }
        }

        return ruleset;
    }

    /* ---------------------------------- */

    public TSLDocSnippet parseTSLDoc(TSLTokenBuffer tokenBuffer) {
        List<TSLToken> tokens = tokenBuffer.getTokens();
        checkNamespaceIntegrity(tokens);

        TSLSymbol begin = (TSLSymbol) tokens.get(0);
        TSLSymbol end = ((TSLSymbol) tokens.get(tokens.size() - 1));

        List<TSLToken> docTokens = tokens.subList(1, tokens.size() - 1);
        List<TSLPlainWord> trimmedDocTokens = new LinkedList<>();

        // Trim trailing * tokens
        int currentLine = begin.getLine();
        for (TSLToken docToken : docTokens) {
            int line = docToken.getLine();
            int character = docToken.getCharacter();
            String rawText = docToken.getRaw();

            if (currentLine != line) {
                if (!rawText.equals("*")) {
                    trimmedDocTokens.add(new TSLPlainWord(line, character, rawText));
                }
                currentLine = line;

            } else {
                trimmedDocTokens.add(new TSLPlainWord(line, character, rawText));
            }
        }

        return new TSLDocSnippet(begin, trimmedDocTokens, end);
    }

    public TSLTagSnippet parseTag(@Nullable Map<String, String> pluginAliases, TSLTokenBuffer tokenBuffer) {
        List<TSLToken> tokens = trimBlockComments(tokenBuffer.getTokens());
        checkNamespaceIntegrity(tokens);

        if (tokens.size() < 2) {
            throw new TSLSyntaxError("Tag snippet missing a tag name", tokenBuffer);
        }

        if (!(tokens.get(1) instanceof TSLPlainWord)) {
            throw new TSLSyntaxError("Tag names MUST be plain strings.", tokens.get(1));
        }

        TSLPlainWord tagNameToken = ((TSLPlainWord) tokens.get(1));
        TSLSymbol tagSymbolToken = (TSLSymbol) tokens.get(0);

        TSLTag tagDefinition = pluginAliases == null
                ? tsl.getTag(tagNameToken)
                : tsl.getTag(pluginAliases, tagNameToken);

        if (tagDefinition == null) {
            throw new TSLSyntaxError(String.format("Unknown tag name -> %s", tagNameToken.getRaw()), tagNameToken);
        }

        List<TSLToken> argTokens = tokens.subList(2, tokens.size());

        return new TSLTagSnippet(
                tagDefinition,
                tagSymbolToken,
                tagNameToken,
                argTokens
        );
    }

    public TSLCaptureSnippet parseCapture(TSLTokenBuffer tokenBuffer) {
        List<TSLToken> tokens = trimBlockComments(tokenBuffer.getTokens());
        checkNamespaceIntegrity(tokens);

        if (tokens.size() < 3) {
            throw new TSLSyntaxError("Malformed capture snippet", tokenBuffer);
        }

        TSLToken captureNameToken = tokens.get(0);

        if (!(captureNameToken instanceof TSLCaptureCall)) {
            throw new TSLSyntaxError(String.format("Invalid capture header -> %s", captureNameToken.getRaw()), tokenBuffer);
        }

        TSLCaptureCall captureCallToken = (TSLCaptureCall) captureNameToken;
        for (TSLToken argName : captureCallToken.getArgs()) {
            if (!TSLTokenizer.VALID_PARAM.matcher(argName.getRaw()).matches()) {
                throw new TSLSyntaxError(String.format("Illegal capture parameter name -> %s", argName), captureCallToken);
            }
        }

        TSLToken equalsSign = tokens.get(1);

        if (!(equalsSign instanceof TSLSymbol) || ((TSLSymbol) equalsSign).getType() != TSLSymbol.Type.CAPTURE_DECLARATION) {
            throw new TSLSyntaxError(String.format("Unexpected token -> %s", equalsSign.getRaw()), tokenBuffer);
        }
        List<TSLToken> capturedTokens = tokens.subList(2, tokens.size());

        return new TSLCaptureSnippet(
                captureCallToken,
                ((TSLSymbol) equalsSign),
                capturedTokens
        );
    }

    public TSLRule parseRule(@Nullable Map<String, String> pluginAliases, @Nullable Map<String, TSLCaptureSnippet> captureSnippets, TSLTokenBuffer buffer) throws TSLSyntaxError {
        TSLRule rule = new TSLRule();
        List<TSLToken> tokens = trimBlockComments(buffer.getTokens());
        checkNamespaceIntegrity(tokens);

        // Fetch index of last Decorator call
        int indexLastDecorator = indexLastDecorator(tokens);

        // Parse and bind Decorators to the rule
        List<TSLDecoratorCall> decoratorCalls = parseDecorators(tokens);

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
        TSLEventSnippet eventSnippet = parseEvent(pluginAliases, tokens, indexOn, indexWith);
        TSLActionSnippet actionSnippet = parseAction(pluginAliases, captureSnippets, tokens, indexLastDecorator, indexOn);
        List<TSLPredicateSnippet> predicateSnippets = parsePredicates(eventSnippet, tokens, indexWith);

        // Compose a rule snippet and bind
        TSLRuleSnippet ruleSnippet = new TSLRuleSnippet(
                decoratorCalls,
                actionSnippet,
                eventSnippet,
                predicateSnippets
        );

        rule.loadSnippet(ruleSnippet);

        // Decorate rule with decorators
        for (TSLDecoratorCall decoratorCall : decoratorCalls) {
            TSLDecorator decorator = parseDecorator(pluginAliases, decoratorCall);
            rule.decorate(decoratorCall, decorator);
        }

        return rule;
    }

    public TSLActionSnippet parseAction(@Nullable Map<String, String> pluginAliases, @Nullable Map<String, TSLCaptureSnippet> captureSnippets, List<TSLToken> tokens, int indexLastDecorator, int indexOn) {
        checkNamespaceIntegrity(tokens);

        List<TSLToken> actionTokens = tokens.subList(indexLastDecorator == -1 ? 0 : indexLastDecorator + 1, indexOn);

        if (actionTokens.size() == 0) {
            throw new TSLSyntaxError("Rule missing action tokens.", tokens.get(0));
        }

        return parseAction(pluginAliases, captureSnippets, actionTokens);
    }

    public TSLActionSnippet parseAction(@Nullable Map<String, String> pluginAliases, @Nullable Map<String, TSLCaptureSnippet> captureSnippets, List<TSLToken> tokens) {
        checkNamespaceIntegrity(tokens);

        TSLToken actionName = tokens.get(0);
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        if (actionName.isCaptureCall()) {
            TSLCaptureCall actionCaptureCall = (TSLCaptureCall) actionName;

            ListAccessor<TSLToken> flattenedTokens = ListAccessor.of(TSLActionSnippet.flatten(tokens, captureSnippets));

            TSLPlainWord flattenedActionName = (TSLPlainWord) flattenedTokens.get(0).filter(TSLToken::isPlainWord)
                    .orElseThrow(() -> new TSLSyntaxError("Expected Action name to be a String Word.",
                            flattenedTokens.get(0).orElseThrow(InternalError::new)));

            List<TSLToken> flattenedArguments = flattenedTokens.subList(1, flattenedTokens.size());

            TSLAction actionDefinition = pluginAliases == null
                    ? tsl.getAction(flattenedActionName)
                    : tsl.getAction(pluginAliases, flattenedActionName);

            if (actionDefinition == null) {
                throw new TSLSyntaxError("Unknown action -> " + flattenedActionName.getRaw(), flattenedActionName);
            }

            Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(flattenedArguments);
            List<TSLToken> actionArgumentsSplit = couple.getFirst();

            actionDefinition.validateTokens(
                    actionName,
                    ListAccessor.of(TSLActionSnippet.flatten(actionArgumentsSplit, captureSnippets)),
                    new TSLParsingContext(this, pluginAliases, captureSnippets));

            return new TSLActionSnippet(
                    actionDefinition,
                    actionCaptureCall,
                    actionArguments,
                    flattenedActionName,
                    flattenedArguments
            );
        }

        if (!actionName.isPlainWord()) {
            throw new TSLSyntaxError("Action name MUST be a String Word.", actionName);
        }

        TSLAction actionDefinition = pluginAliases == null
                ? tsl.getAction(((TSLPlainWord) actionName))
                : tsl.getAction(pluginAliases, ((TSLPlainWord) actionName));

        if (actionDefinition == null) {
            throw new TSLSyntaxError("Unknown action -> " + actionName.getRaw(), actionName);
        }

        Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(actionArguments);
        List<TSLToken> actionArgumentsSplit = couple.getFirst();

        actionDefinition.validateTokens(
                actionName,
                ListAccessor.of(TSLActionSnippet.flatten(actionArgumentsSplit, captureSnippets)),
                new TSLParsingContext(this, pluginAliases, captureSnippets));

        return new TSLActionSnippet(
                actionDefinition,
                ((TSLPlainWord) actionName),
                actionArguments
        );
    }

    public TSLEventSnippet parseEvent(@Nullable Map<String, String> pluginAliases, List<TSLToken> tokens, int indexOn, int indexWith) {
        checkNamespaceIntegrity(tokens);

        List<TSLPlainWord> eventTokens = getEventNameTokens(tokens, indexOn, indexWith);

        String eventName = eventTokens.stream()
                .map(TSLPlainWord::getRaw)
                .collect(Collectors.joining(" "));

        TSLEvent eventDefinition = pluginAliases == null
                ? tsl.getEvent(eventTokens)
                : tsl.getEvent(pluginAliases, eventTokens);

        if (eventDefinition == null) {
            throw new TSLSyntaxError("Unknown event name -> " + eventName, eventTokens.get(0));
        }

        return new TSLEventSnippet(
                eventDefinition,
                ((TSLPlainWord) tokens.get(indexOn)),
                eventTokens);
    }

    public List<TSLPredicateSnippet> parsePredicates(TSLEventSnippet eventSnippet, List<TSLToken> tokens, int indexWith) {
        if (indexWith == -1) {
            return Collections.emptyList();
        }

        List<TSLPredicateSnippet> predicateSnippets = new LinkedList<>();

        List<TSLToken> predicatePart = tokens.subList(indexWith, tokens.size());
        List<TSLToken> predicateTokens = new LinkedList<>();

        for (int i = 0; i < predicatePart.size(); i++) {
            TSLToken token = predicatePart.get(i);
            boolean isLastToken = i == predicatePart.size() - 1;
            if (isLastToken || token instanceof TSLPlainWord && token.getRaw().equalsIgnoreCase("WITH")) {
                if (isLastToken) predicateTokens.add(token);
                if (!predicateTokens.isEmpty()) {
                    predicateSnippets.add(parsePredicate(eventSnippet, predicateTokens));
                    predicateTokens = new LinkedList<>();
                }
            }
            predicateTokens.add(token);
        }

        return predicateSnippets;
    }

    public TSLPredicateSnippet parsePredicate(TSLEventSnippet eventSnippet, List<TSLToken> tokens) {
        checkNamespaceIntegrity(tokens);

        TSLToken withToken = tokens.get(0);

        if (!(withToken instanceof TSLPlainWord)) {
            throw new TSLSyntaxError("Predicates MUST start with WITH keyword.", withToken);
        }

        if (!((TSLPlainWord) withToken).getRawWord().equalsIgnoreCase("WITH")) {
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

        return new TSLPredicateSnippet(
                matchingPredicateFormat.get(),
                ((TSLPlainWord) withToken),
                predicateTokens
        );
    }

    public List<TSLDecoratorCall> parseDecorators(List<TSLToken> tokens) {
        checkNamespaceIntegrity(tokens);

        int indexLastDecorator = indexLastDecorator(tokens);

        return tokens.subList(0, indexLastDecorator == -1 ? 0 : indexLastDecorator + 1)
                .stream().map(token -> ((TSLDecoratorCall) token))
                .collect(Collectors.toList());
    }

    public TSLDecorator parseDecorator(@Nullable Map<String, String> pluginAliases, TSLDecoratorCall decoratorCall) {
        TSLDecorator decorator = tsl.getDecorator(pluginAliases, decoratorCall);

        if (decorator == null) {
            throw new TSLSyntaxError("Unknown decorator name -> " + decoratorCall.getName(), decoratorCall);
        }

        return decorator;
    }

    /* ---------------------------------- */

    public static void checkNamespaceIntegrity(List<TSLToken> tokens) {
        for (TSLToken token : tokens) {
            String text = token.getRaw();
            if (token instanceof TSLPlainWord && StringUtils.occurrenceCount(text, '.') >= 2) {
                throw new TSLSyntaxError("Cannot have multiple namespacing delimiters", token);
            }
        }
    }

    /* ---------------------------------- */

    public static List<TSLToken> trimMultilineComments(List<TSLToken> tokens) {
        return trimBlock(tokens,
                token -> TSLSymbol.equals(token, TSLSymbol.Type.MULTI_LINE_COMMENT_BEGIN),
                token -> TSLSymbol.equals(token, TSLSymbol.Type.MULTI_LINE_COMMENT_END));
    }

    public static List<TSLToken> trimBlockComments(List<TSLToken> tokens) {
        return trimBlock(tokens,
                token -> TSLSymbol.equals(token, TSLSymbol.Type.MULTI_LINE_COMMENT_BEGIN) || TSLSymbol.equals(token, TSLSymbol.Type.TSLDOC_BEGIN),
                token -> TSLSymbol.equals(token, TSLSymbol.Type.MULTI_LINE_COMMENT_END));
    }

    public static List<TSLToken> trimBlock(List<TSLToken> tokens, Predicate<TSLToken> isBegin, Predicate<TSLToken> isEnd) {
        List<TSLToken> trimmedTokens = new LinkedList<>();
        boolean inBlock = false;

        for (TSLToken token : tokens) {
            if (isBegin.test(token)) {
                inBlock = true;
                continue;
            }

            if (isEnd.test(token)) {
                inBlock = false;
                continue;
            }

            if (inBlock) {
                continue;
            }

            trimmedTokens.add(token);
        }

        return trimmedTokens;
    }

    /* ---------------------------------- */

    /* --------------------------- */

    private static int indexOfKeyword(List<TSLToken> tokens, String keyword) {
        return CollectionUtils.indexOfBy(tokens,
                token -> token.getRaw().equalsIgnoreCase(keyword));
    }

    private static int indexLastDecorator(List<TSLToken> tokens) {
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

    public static List<TSLPlainWord> getEventNameTokens(List<TSLToken> tokens, int indexOn, int indexWith) {
        List<TSLToken> eventTokens = tokens.subList(indexOn + 1,
                indexWith == -1 ? tokens.size() : indexWith);

        if (eventTokens.size() == 0) {
            TSLToken keywordOn = tokens.get(indexOn);
            throw new TSLSyntaxError("Missing event name.", keywordOn);
        }

        List<TSLPlainWord> eventTokensAsString = new LinkedList<>();
        for (TSLToken token : eventTokens) {
            if (!(token instanceof TSLPlainWord)) {
                throw new TSLSyntaxError("Event statements MUST contain only String Words. Instead found -> " + token.getTypeName() + " " + token.getRaw(), token);
            }
            eventTokensAsString.add(((TSLPlainWord) token));
        }

        return eventTokensAsString;
    }

}
