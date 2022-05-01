package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.snippet.*;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import net.programmer.igoodie.tsl.util.IOUtils;
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

        for (TSLTokenBuffer buffer : lexer.getSnippets()) {
            if (buffer.getType() == TSLTokenBuffer.Type.TAG) {
                ruleset.addTag(parseTag(ruleset, buffer));

            } else if (buffer.getType() == TSLTokenBuffer.Type.CAPTURE) {
                ruleset.addCapture(parseCapture(ruleset, buffer));

            } else if (buffer.getType() == TSLTokenBuffer.Type.RULE) {
                ruleset.addRule(parseRule(ruleset, buffer));

            } else if (buffer.getType() == TSLTokenBuffer.Type.TSLDOC) {
                ruleset.addTSLDoc(parseTSLDoc(ruleset, buffer));
            }
        }

        return ruleset;
    }

    /* --------------------------- */

    public TSLDocSnippet parseTSLDoc(TSLRuleset ruleset, TSLTokenBuffer buffer) {
        List<TSLToken> tokens = buffer.getTokens();

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

        return new TSLDocSnippet(ruleset, begin, trimmedDocTokens, end);
    }

    public TSLTagSnippet parseTag(TSLRuleset ruleset, TSLTokenBuffer buffer) throws TSLSyntaxError {
        List<TSLToken> tokens = trimBlockComments(buffer.getTokens());

        if (tokens.size() < 2) {
            throw new TSLSyntaxError("Tag snippet missing a tag name", buffer);
        }

        if (!(tokens.get(1) instanceof TSLPlainWord)) {
            throw new TSLSyntaxError("Tag names MUST be plain strings.", tokens.get(1));
        }

        TSLPlainWord tagNameToken = ((TSLPlainWord) tokens.get(1));
        TSLSymbol tagSymbolToken = (TSLSymbol) tokens.get(0);

        TSLTag tagDefinition = tsl.getTag(ruleset, tagNameToken);

        if (tagDefinition == null) {
            throw new TSLSyntaxError(String.format("Unknown tag name -> %s", tagNameToken.getRaw()), tagNameToken);
        }

        List<TSLToken> argTokens = tokens.subList(2, tokens.size());

        return new TSLTagSnippet(ruleset,
                tagDefinition,
                tagSymbolToken,
                tagNameToken,
                argTokens);
    }

    public TSLCaptureSnippet parseCapture(TSLRuleset ruleset, TSLTokenBuffer buffer) throws TSLSyntaxError {
        List<TSLToken> tokens = trimBlockComments(buffer.getTokens());

        if (tokens.size() < 3) {
            throw new TSLSyntaxError("Malformed capture snippet", buffer);
        }

        TSLToken captureNameToken = tokens.get(0);

        if (!(captureNameToken instanceof TSLCaptureCall)) {
            throw new TSLSyntaxError(String.format("Invalid capture header -> %s", captureNameToken.getRaw()), buffer);
        }

        TSLCaptureCall captureCallToken = (TSLCaptureCall) captureNameToken;
        for (String argName : captureCallToken.getArgs()) {
            if (!TSLTokenizer.VALID_PARAM.matcher(argName).matches()) {
                throw new TSLSyntaxError(String.format("Illegal capture parameter name -> %s", argName), captureCallToken);
            }
        }

        TSLToken equalsSign = tokens.get(1);

        if (!(equalsSign instanceof TSLSymbol) || ((TSLSymbol) equalsSign).getType() != TSLSymbol.Type.CAPTURE_DECLARATION) {
            throw new TSLSyntaxError(String.format("Unexpected token -> %s", equalsSign.getRaw()), buffer);
        }
        List<TSLToken> capturedTokens = tokens.subList(2, tokens.size());

        return new TSLCaptureSnippet(ruleset,
                captureCallToken,
                ((TSLSymbol) equalsSign),
                capturedTokens);
    }

    public TSLRule parseRule(TSLRuleset ruleset, TSLTokenBuffer buffer) throws TSLSyntaxError {
        TSLRule rule = new TSLRule();
        List<TSLToken> tokens = trimBlockComments(buffer.getTokens());

        System.out.println(tokens);

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
        TSLPlainWord actionNameToken = actionSnippet.getActionNameToken();
        TSLAction actionDefinition = actionSnippet.getActionDefinition();

        Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(actionSnippet.getActionTokens());
        List<TSLToken> actionTokens = couple.getFirst();
        TSLToken messageToken = couple.getSecond();

        // Validate actions arguments
        actionDefinition.validateTokens(actionNameToken, actionTokens, rule, this);

        return rule;
    }

    public TSLEventSnippet parseEvent(TSLRuleset ruleset, List<TSLToken> tokens, int indexOn, int indexWith) {
        List<TSLPlainWord> eventTokens = getEventNameTokens(tokens, indexOn, indexWith);

        String eventName = eventTokens.stream()
                .map(TSLPlainWord::getRaw)
                .collect(Collectors.joining(" "));

        TSLEvent eventDefinition = tsl.getEvent(ruleset, eventTokens);

        if (eventDefinition == null) {
            throw new TSLSyntaxError("Unknown event name -> " + eventName, eventTokens.get(0));
        }

        return new TSLEventSnippet(ruleset, eventDefinition,
                ((TSLPlainWord) tokens.get(indexOn)),
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
        List<TSLToken> actionArguments = tokens.subList(1, tokens.size());

        if (!(actionName instanceof TSLPlainWord)) {
            throw new TSLSyntaxError("Action name MUST be a String Word.", actionName);
        }

        TSLAction actionDefinition = tsl.getAction(ruleset, ((TSLPlainWord) actionName));

        if (actionDefinition == null) {
            throw new TSLSyntaxError("Unknown action -> " + actionName.getRaw(), actionName);
        }

        Couple<List<TSLToken>, TSLToken> couple = actionDefinition.splitByDisplaying(actionArguments);
        List<TSLToken> actionArgumentsSplitted = couple.getFirst();

        actionDefinition.validateTokens(actionName, actionArgumentsSplitted, null, this);

        return new TSLActionSnippet(ruleset, actionDefinition,
                ((TSLPlainWord) actionName), actionArguments);
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
            if (isLastToken || token instanceof TSLPlainWord && token.getRaw().equalsIgnoreCase("WITH")) {
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

        if (!(withToken instanceof TSLPlainWord)) {
            throw new TSLSyntaxError("Predicates MUST start with WITH keyword.", withToken);
        }

        if (!((TSLPlainWord) withToken).getWord().equalsIgnoreCase("WITH")) {
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
                ((TSLPlainWord) withToken),
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
            rule.decorate(decorator, decoratorToken);
        }

        return decoratorTokens;
    }

    public TSLDecorator parseDecorator(TSLDecoratorCall decoratorCall) {
        TSLDecorator decorator = tsl.getDecorator(ruleset, decoratorCall);

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

    /* --------------------------- */

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
