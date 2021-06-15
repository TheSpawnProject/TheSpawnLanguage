package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;
import net.programmer.igoodie.tsl.util.ExpressionUtils;

import java.util.*;

// [$captureName] [=] [DROP apple]
// [$captureName{x,y}] [=] [DROP {x} {y}]
public class TSLCaptureSnippet extends TSLSnippet {

    protected TSLCaptureCall header;
    protected List<TSLToken> capturedTokens;

    public TSLCaptureSnippet(TSLRuleset ruleset, TSLCaptureCall header, TSLSymbol equalsSign, List<TSLToken> tokens) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, header, equalsSign, tokens));
        this.header = header;
        this.capturedTokens = tokens;
    }

    public TSLCaptureCall getHeader() {
        return header;
    }

    public String getName() {
        return header.getCaptureName();
    }

    public List<String> getParameterNames() {
        return header.getArgs();
    }

    public List<TSLToken> getCapturedTokens() {
        return capturedTokens;
    }

    /* -------------------------------------------- */

    public Map<String, TSLToken> argumentsToMap(List<TSLToken> arguments) {
        List<String> parameterNames = getParameterNames();

        if (parameterNames.size() > arguments.size()) {
            throw new IllegalArgumentException("Insufficient number of arguments...");
        }

        HashMap<String, TSLToken> argumentMap = new HashMap<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            argumentMap.put(parameterNames.get(i), arguments.get(i));
        }

        return argumentMap;
    }

    /* -------------------------------------------- */

    public List<TSLToken> flattenInline(String... arguments) {
        TSLTokenizer tokenizer = new TSLTokenizer();
        return flatten(tokenizer.tokenizeAll(arguments));
    }

    public List<TSLToken> flattenInline(List<String> arguments) {
        TSLTokenizer tokenizer = new TSLTokenizer();
        return flatten(tokenizer.tokenizeAll(arguments));
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
                List<TSLToken> flattenedCapture = captureSnippet.flattenInline(captureCall.getArgs());
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

        } else if (target instanceof TSLString) {
            String filled = fillWithParameters(((TSLString) target).getWord(), argumentMap);
            return new TSLString(target.getLine(), target.getCharacter(), filled);

        } else if (target instanceof TSLGroup) {
            String filled = fillWithParameters(((TSLGroup) target).getGroup(), argumentMap);
            return new TSLGroup(target.getLine(), target.getCharacter(), filled);

        } else if (target instanceof TSLExpression) {
            String filled = fillWithParameters(((TSLExpression) target).getExpression(), argumentMap);
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
