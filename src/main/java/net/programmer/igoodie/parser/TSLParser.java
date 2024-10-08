package net.programmer.igoodie.parser;

import net.programmer.igoodie.TSLPlatform;
import net.programmer.igoodie.exception.TSLSyntaxException;
import net.programmer.igoodie.runtime.action.TSLAction;
import net.programmer.igoodie.runtime.event.TSLEvent;
import net.programmer.igoodie.runtime.predicate.TSLPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TSLParser {

    private final TSLPlatform platform;
    private final List<TSLLexer.Token> tokens;
    private int index;

    public TSLParser(TSLPlatform platform, List<TSLLexer.Token> tokens) {
        this.platform = platform;
        this.tokens = tokens;
        this.index = 0;
    }

    public Ruleset parse() throws TSLSyntaxException {
        Ruleset ruleset = new Ruleset();

        parseEmptyLine(); // Skip leading empty lines

        while (index < tokens.size()) {
            Rule rule = parseRule();
            ruleset.addRule(rule);
            parseEmptyLine(); // Skip empty lines between rules
        }

        return ruleset;
    }

    private Rule parseRule() throws TSLSyntaxException {
        String actionName = parseWord();
        if (actionName == null)
            throw new TSLSyntaxException("Expected action name.");

        List<String> actionArgs = parseActionArgs();

        TSLAction.Supplier<?> actionDefinition = platform.getActionDefinition(actionName)
                .orElseThrow(() -> new TSLSyntaxException("Unknown action -> {}", actionName));

        TSLAction action = actionDefinition.generate(actionArgs);

        if (!consume(token -> token.type == TSLLexer.TokenType.KEYWORD_ON))
            throw new TSLSyntaxException("Expected 'ON' after action part.");

        TSLEvent event = parseEvent();
        if (event == null)
            throw new TSLSyntaxException("Expected event name.");

        List<TSLPredicate> predicates = parsePredicates();
        return new Rule(action, actionArgs, event, predicates);
    }

    private List<String> parseActionArgs() {
        List<String> args = new ArrayList<>();
        String word;
        while ((word = parseWord()) != null) {
            args.add(word);
        }
        return args;
    }

    private TSLEvent parseEvent() throws TSLSyntaxException {
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

    private List<TSLPredicate> parsePredicates() {
        List<TSLPredicate> predicates = new ArrayList<>();
        while (consume(token -> token.type == TSLLexer.TokenType.KEYWORD_WITH)) {
            List<String> words = new ArrayList<>();
            String word;
            while ((word = parseWord()) != null) {
                words.add(word);
            }
            // TODO
//            predicates.add(new TSLPredicate(words));
        }
        return predicates;
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

    public static class Ruleset {
        private final List<Rule> rules = new ArrayList<>();

        public void addRule(Rule rule) {
            rules.add(rule);
        }
    }

    public static class Rule {
        private final TSLAction action;
        private final List<String> actionArgs;
        private final TSLEvent event;
        private final List<TSLPredicate> predicates;

        public Rule(TSLAction action, List<String> actionArgs, TSLEvent eventName, List<TSLPredicate> predicates) {
            this.action = action;
            this.actionArgs = actionArgs;
            this.event = eventName;
            this.predicates = predicates;
        }
    }

//    public static void main(String[] args) throws TSLSyntaxException {
//        // Example token list
//        List<TSLLexer.Token> tokens = Arrays.asList(
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "PRINT"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "Hello"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "World"),
//                new TSLLexer.Token(TSLLexer.TokenType.KEYWORD_ON, "ON"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "Twitch"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "Follow"),
//                new TSLLexer.Token(TSLLexer.TokenType.KEYWORD_WITH, "WITH"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "amount"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "="),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "100"),
//
//                new TSLLexer.Token(TSLLexer.TokenType.EMPTY_LINE, "\n"),
//
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "PRINT"),
////                new TSLLexer.Token(TSLLexer.TokenType.WORD, "Hello"),
////                new TSLLexer.Token(TSLLexer.TokenType.WORD, "World"),
//                new TSLLexer.Token(TSLLexer.TokenType.KEYWORD_ON, "ON"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "Twitch"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "Follow"),
//                new TSLLexer.Token(TSLLexer.TokenType.KEYWORD_WITH, "WITH"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "condition1"),
//                new TSLLexer.Token(TSLLexer.TokenType.WORD, "condition2")
//        );
//
//        TSLParser parser = new TSLParser(platform, tokens);
//        Ruleset ruleset = parser.parse();
//        // Process the parsed ruleset as needed
//        System.out.println("Parsed ruleset: " + ruleset);
//    }
}
