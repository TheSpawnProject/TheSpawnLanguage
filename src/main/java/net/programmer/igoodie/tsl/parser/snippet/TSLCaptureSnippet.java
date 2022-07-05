package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.ExpressionUtils;

import java.util.*;

// [$captureName] [=] [DROP apple]
// [$captureName(x,y)] [=] [DROP {x} {y}]
public class TSLCaptureSnippet extends TSLSnippet {

    protected TSLCaptureCall header;
    protected TSLSymbol equalsSign;
    protected List<TSLToken> capturedTokens;

    public TSLCaptureSnippet(TSLRuleset ruleset, TSLCaptureCall header, TSLSymbol equalsSign, List<TSLToken> tokens) {
        super(ruleset, flatTokens(header, equalsSign, tokens));
        this.header = header;
        this.equalsSign = equalsSign;
        this.capturedTokens = tokens;
    }

    public TSLCaptureCall getHeaderToken() {
        return header;
    }

    public TSLSymbol getEqualsSignToken() {
        return equalsSign;
    }

    public List<TSLToken> getCapturedTokens() {
        return capturedTokens;
    }

    /* -------------------------------------------- */

    public String getName() {
        return header.getCaptureName();
    }

    public List<TSLToken> getParameterNames() {
        return header.getArgs();
    }

    /* -------------------------------------------- */

    public Map<String, TSLToken> argumentsToMap(List<TSLToken> arguments) {
        List<TSLToken> parameterNames = getParameterNames();

        if (parameterNames.size() > arguments.size()) {
            throw new IllegalArgumentException("Insufficient number of arguments...");
        }

        HashMap<String, TSLToken> argumentMap = new HashMap<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            argumentMap.put(parameterNames.get(i).getRaw(), arguments.get(i));
        }

        return argumentMap;
    }

    /* -------------------------------------------- */

    public List<TSLToken> tokenizeAndFlatten(String... arguments) {
        return tokenizeAndFlatten(Arrays.asList(arguments));
    }

    public List<TSLToken> tokenizeAndFlatten(List<String> arguments) {
        TSLTokenizer tokenizer = new TSLTokenizer();
        List<TSLToken> tokens = tokenizer.tokenizeAll(arguments);
        for (TSLToken token : tokens) {
            if (tokenizer.tokenCount(token.getRaw()) != 1) {
                throw new IllegalArgumentException("Invalid argument passed -> " + token.getRaw());
            }
        }
        return flatten(tokens);
    }

    public List<TSLToken> flatten(TSLToken... arguments) {
        return flatten(Arrays.asList(arguments));
    }

    public List<TSLToken> flatten(List<TSLToken> arguments) {
        return flatten(argumentsToMap(arguments));
    }

    public List<TSLToken> flatten(Map<String, TSLToken> argumentMap) {
        List<TSLToken> replaced = new LinkedList<>();

        for (TSLToken capturedToken : this.capturedTokens) {
            if (capturedToken instanceof TSLCaptureCall) {
                // TODO: Disallow circular capture calls (might need a stack trace...)
                TSLCaptureCall captureCall = (TSLCaptureCall) capturedToken;
                if (captureCall.getCaptureName().equals(this.getName())) {
                    throw new TSLRuntimeError("Captures MUST not call themselves recursively.", captureCall);
                }
                TSLCaptureSnippet captureSnippet = ruleset.getCaptureSnippet(captureCall);
                List<TSLToken> flattenedCapture = captureSnippet.flatten(captureCall.getArgs());
                replaced.addAll(flattenedCapture);

            } else {
                TSLToken parameterizedToken = fillWithParameters(capturedToken, argumentMap);
                replaced.add(parameterizedToken);
            }
        }

        return replaced;
    }

    public static TSLToken fillWithParameters(TSLToken target, Map<String, TSLToken> argumentMap) {
        if (target instanceof TSLCaptureParameter) {
            TSLCaptureParameter parameterToken = (TSLCaptureParameter) target;
            return argumentMap.get(parameterToken.getParameterName());

        } else if (target instanceof TSLGroup) {
            TSLGroup groupToken = (TSLGroup) target;
            String groupExpression = StringUtilities.shrink(groupToken.getRaw(), 1, 1);
            String filled = fillWithParameters(groupExpression, argumentMap);
            List<TSLToken> filledGroupTokens = TSLLexer.lexGroupTokens(filled);
            return new TSLGroup(target.getLine(), target.getCharacter(), filledGroupTokens);

        } else if (target instanceof TSLExpression) {
            TSLExpression expressionToken = (TSLExpression) target;
            String filled = fillWithParameters(expressionToken.getExpression(), argumentMap);
            return new TSLExpression(target.getLine(), target.getCharacter(), filled);
        }

        return target;
    }

    public static String fillWithParameters(String target, Map<String, TSLToken> argumentMap) {
        return ExpressionUtils.replaceCaptureParams(target, parameterName -> {
            TSLToken argument = argumentMap.get(parameterName);
            if (argument == null) return "{{" + parameterName + "}}";
            return argument.getRaw();
        });
    }

}
