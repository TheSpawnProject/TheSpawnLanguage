package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.parser.snippet.TSLSnippetBuffer;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.util.StringUtilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TSLTokenizer {

    public static final Pattern RULE_DECORATOR_PATTERN = Pattern.compile("@(?<name>[\\w_-]+)(?<args>\\(\\w+(,\\w+)*\\))?");
    public static final Pattern CAPTURE_CALL_PATTERN = Pattern.compile("(?<name>\\$[\\w_-]+)(?<args>\\(([^\\[,]+,?)*\\))?");

    public TSLToken tokenize(String text, int line, int character) {
        if (text.startsWith("%") && text.endsWith("%")) {
            String groupFragment = StringUtilities.shrink(text, 1, 1);
            return new TSLGroup(line, character, groupFragment);
        }

        if (text.startsWith("${") && text.endsWith("}")) {
            return new TSLExpression(line, character, text.substring(2, text.length() - 1));
        }

        Matcher captureMatcher = CAPTURE_CALL_PATTERN.matcher(text);
        if (captureMatcher.matches()) {
            String name = captureMatcher.group("name");
            String[] args = captureMatcher.group("args") == null
                    ? new String[0] : StringUtilities.shrink(captureMatcher.group("args"), 1, 1).split(",\\s*");
            return new TSLCaptureCall(line, character, name.substring(1), Arrays.asList(args));
        }

        Matcher decoratorMatcher = RULE_DECORATOR_PATTERN.matcher(text);
        if (decoratorMatcher.matches()) {
            String name = decoratorMatcher.group("name");
            String[] args = decoratorMatcher.group("args") == null ? new String[0] : decoratorMatcher.group("args").split(",\\s*");
            return new TSLDecoratorCall(line, character, name, Arrays.asList(args));
        }

        if (text.startsWith("{{") && text.endsWith("}}")) {
            return new TSLCaptureParameter(line, character, text.substring(2, text.length() - 2));
        }

        if (text.matches("\\w+")) {
            return new TSLString(line, character, text);
        }

        for (TSLSymbol.Type symbolType : TSLSymbol.Type.values()) {
            if (symbolType.getSymbol().equals(text)) {
                return new TSLSymbol(line, character, symbolType);
            }
        }

        if (text.startsWith("(") && text.endsWith(")")) {
            String nestFragment = StringUtilities.shrink(text, 1, 1);
            TSLLexer subLexer = new TSLLexer(nestFragment).withOffset(line - 1, character);
            subLexer.lex();
            return new TSLNest(line, character, subLexer.getSnippets().get(0).getTokens());
        }

        return new TSLString(line, character, text);
    }

    public TSLToken tokenize(String text) {
        return tokenize(text, 0, 0);
    }

    public List<TSLToken> tokenizeAll(String... texts) {
        return tokenizeAll(Arrays.asList(texts));
    }

    public List<TSLToken> tokenizeAll(List<String> texts) {
        return texts.stream().map(this::tokenize).collect(Collectors.toList());
    }

    public long tokenCount(String text) {
        TSLLexer lexer = new TSLLexer(text);
        lexer.lex();
        return lexer.getSnippets().stream()
                .map(TSLSnippetBuffer::getTokens)
                .mapToLong(Collection::size)
                .sum();
    }

}
