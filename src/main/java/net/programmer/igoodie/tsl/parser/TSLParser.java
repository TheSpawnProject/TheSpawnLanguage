package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.runtime.predicate.TSLComparator;
import net.programmer.igoodie.tsl.runtime.predicate.TSLPredicate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TSLParser {

    private final TSLPlatform platform;
    private final String target;
    private final List<TSLLexer.Token> tokens;
    private int index;

    public TSLParser(TSLPlatform platform, String target, List<TSLLexer.Token> tokens) {
        this.platform = platform;
        this.target = target;
        this.tokens = tokens;
        this.index = 0;
    }

    public static TSLParser immediate(TSLPlatform platform, List<String> calculatedTokens) throws TSLSyntaxException {
        try {
            CharStream charStream = CharStream.fromString(String.join(" ", calculatedTokens));
            TSLLexer lexer = new TSLLexer(charStream);
            return new TSLParser(platform, "immediate", lexer.tokenize());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TSLRuleset parse() throws TSLSyntaxException {
        TSLRuleset ruleset = new TSLRuleset(target);

        parseEmptyLine(); // Skip leading empty lines

        while (index < tokens.size()) {
            TSLRule rule = parseRule();
            ruleset.addRule(rule);
            parseEmptyLine(); // Skip empty lines between rules
        }

        return ruleset;
    }

    protected TSLRule parseRule() throws TSLSyntaxException {
        TSLAction action = parseAction();

        if (!consume(token -> token.type == TSLLexer.TokenType.KEYWORD_ON))
            throw new TSLSyntaxException("Expected 'ON' after action part.");

        TSLEvent event = parseEvent();
        if (event == null)
            throw new TSLSyntaxException("Expected event name.");

        List<TSLPredicate> predicates = parsePredicates();

        TSLRule rule = new TSLRule(event);
        rule.setAction(action);
        for (TSLPredicate predicate : predicates) {
            rule.addPredicate(predicate);
        }

        return rule;
    }

    public TSLAction parseAction() throws TSLSyntaxException {
        String actionName = parseWord();

        if (actionName == null) {
            throw new TSLSyntaxException("Expected action name.");
        }

        List<String> actionArgs = parseActionArgs();

        TSLAction.Supplier<?> actionDefinition = platform.getActionDefinition(actionName)
                .orElseThrow(() -> new TSLSyntaxException("Unknown action -> {}", actionName));

        return actionDefinition.generate(platform, actionArgs);
    }

    private List<String> parseActionArgs() {
        List<String> args = new ArrayList<>();
        String word;
        while ((word = parseWord()) != null) {
            args.add(word);
        }
        return args;
    }

    public TSLEvent parseEvent() throws TSLSyntaxException {
        StringBuilder eventName = new StringBuilder();
        String word = parseWord();
        if (word == null) return null;

        eventName.append(word);

        TSLLexer.Token token;
        while ((token = getToken(index)) != null && token.type == TSLLexer.TokenType.WORD) {
            eventName.append(" ").append(parseWord());
        }

        return platform.getEvent(eventName.toString())
                .orElseThrow(() -> new TSLSyntaxException("Unknown event -> {}", eventName.toString()));
    }

    private List<TSLPredicate> parsePredicates() throws TSLSyntaxException {
        List<TSLPredicate> predicates = new ArrayList<>();
        while (consume(token -> token.type == TSLLexer.TokenType.KEYWORD_WITH)) {
            predicates.add(parsePredicate());
        }
        return predicates;
    }

    public TSLPredicate parsePredicate() throws TSLSyntaxException {
        List<String> words = new ArrayList<>();

        String word;
        while ((word = parseWord()) != null) {
            words.add(word);
        }

        if (words.size() < 3) {
            throw new TSLSyntaxException("");
        }

        String fieldName = words.get(0);
        String comparatorSymbol = String.join(" ", words.subList(1, words.size() - 1));
        String right = words.get(words.size() - 1);

        TSLComparator.Supplier<?> comparatorDefinition = platform.getComparatorDefinition(comparatorSymbol)
                .orElseThrow(() -> new TSLSyntaxException("Unknown comparator -> {}", comparatorSymbol));

        TSLComparator comparator = comparatorDefinition.generate(right);
        return new TSLPredicate(fieldName, comparator);
    }

    private String parseEmptyLine() {
        return consume(token -> token.type == TSLLexer.TokenType.EMPTY_LINE,
                token -> token.value,
                () -> null);
    }

    private String parseWord() {
        return consume(token -> token.type == TSLLexer.TokenType.WORD,
                token -> token.value,
                () -> null);
    }

    private TSLLexer.Token getToken(int index) {
        if (index >= tokens.size()) return null;
        return tokens.get(index);
    }

    private boolean consume(Predicate<TSLLexer.Token> predicate) {
        return consume(predicate, token -> true, () -> false);
    }

    private <T> T consume(Predicate<TSLLexer.Token> predicate, Function<TSLLexer.Token, T> result, Supplier<T> orElse) {
        TSLLexer.Token token = getToken(index);
        if (token == null) return orElse.get();
        if (predicate.test(token)) {
            index++; // Move to the next token
            return result.apply(token);
        }
        return orElse.get(); // Return the default value if predicate fails
    }

}
