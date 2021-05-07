package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TSLTokenizer {

    public static final Pattern RULE_DECORATOR_PATTERN = Pattern.compile("@(?<name>\\w+)(?<args>\\(\\w+(,\\w+)*\\))?");
    public static final Pattern CAPTURE_CALL_PATTERN = Pattern.compile("(?<name>\\$\\w+)(?<args>\\{([^\\[,]+,?)*})?");

    public TSLToken tokenize(String text, int line, int character) {
        if (text.startsWith("%") && text.endsWith("%")) {
            return new TSLGroup(line, character, text.substring(1, text.length() - 1));
        }

        if (text.startsWith("${") && text.endsWith("}")) {
            return new TSLExpression(line, character, text.substring(2, text.length() - 1));
        }

        Matcher captureMatcher = CAPTURE_CALL_PATTERN.matcher(text);
        if (captureMatcher.matches()) {
            String name = captureMatcher.group("name");
            String[] args = captureMatcher.group("args") == null
                    ? new String[0] : StringUtils.shrink(captureMatcher.group("args"), 1, 1).split(",");
            return new TSLCaptureCall(line, character, name.substring(1), Arrays.asList(args));
        }

        Matcher decoratorMatcher = RULE_DECORATOR_PATTERN.matcher(text);
        if (decoratorMatcher.matches()) {
            String name = decoratorMatcher.group("name");
            String[] args = decoratorMatcher.group("args") == null ? new String[0] : decoratorMatcher.group("args").split(",");
            return new TSLDecoratorCall(line, character, name, Arrays.asList(args));
        }

        if (text.startsWith("{") && text.endsWith("}")) {
            return new TSLCaptureParameter(line, character, text.substring(1, text.length() - 1));
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
            TSLLexer subLexer = new TSLLexer(text.substring(1, text.length() - 1)).withOffset(line - 1, character);
            subLexer.lex();
            return new TSLNest(line, character, subLexer.getSnippets().get(0).getTokens());
        }

        throw new TSLSyntaxError("Unknown token format (" + text + ")", line, character);
    }

    public TSLToken tokenize(String text) {
        return tokenize(text, 0, 0);
    }

    public static List<TSLToken> tokenizeAll(TSLCaptureCall captureCall) {
        return tokenizeAll(captureCall.getArgs(), captureCall.getLine(), captureCall.getCharacter());
    }

    public static List<TSLToken> tokenizeAll(List<String> texts, int line, int character) {
        TSLTokenizer tokenizer = new TSLTokenizer();
        return texts.stream()
                .map(text -> tokenizer.tokenize(text, line, character))
                .collect(Collectors.toList());
    }

}
