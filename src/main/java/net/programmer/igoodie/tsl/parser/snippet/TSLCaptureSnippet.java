package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.lexer.TSLLexer;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.util.PatternUtils;

import java.util.*;

// [$captureName] [=] [DROP apple]
// [$captureName(x,y)] [=] [DROP {{x}} {{y}}]
public class TSLCaptureSnippet extends TSLSnippet {

    protected TSLCaptureCall header;
    protected TSLSymbol equalsSign;
    protected List<TSLToken> capturedTokens;

    public TSLCaptureSnippet(TSLCaptureCall header, TSLSymbol equalsSign, List<TSLToken> tokens) {
        super(flatTokens(header, equalsSign, tokens));
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

    public List<TSLToken> fill(Map<String, TSLCaptureSnippet> captureSnippets, TSLToken... arguments) {
        return fill(captureSnippets, Arrays.asList(arguments));
    }

    public List<TSLToken> fill(Map<String, TSLCaptureSnippet> captureSnippets, List<TSLToken> arguments) {
        return fill(captureSnippets, argumentsToMap(arguments));
    }

    public List<TSLToken> fill(Map<String, TSLCaptureSnippet> captureSnippets, Map<String, TSLToken> argumentMap) {
        List<TSLToken> replaced = new LinkedList<>();

        for (TSLToken capturedToken : this.capturedTokens) {
            if (capturedToken instanceof TSLCaptureCall) {
                // TODO: Disallow circular capture calls (might need a stack trace...)
                TSLCaptureCall captureCall = (TSLCaptureCall) capturedToken;
                if (captureCall.getCaptureName().equals(this.getName())) {
                    throw new TSLRuntimeError("Captures MUST not call themselves recursively.", captureCall);
                }
                TSLCaptureSnippet captureSnippet = captureSnippets.get(captureCall.getCaptureName());
                List<TSLToken> flattenedCapture = captureSnippet.fill(captureSnippets, captureCall.getArgs());
                replaced.addAll(flattenedCapture);

            } else {
                TSLToken parameterizedToken = fillSingleToken(capturedToken, argumentMap);
                replaced.add(parameterizedToken);
            }
        }

        return replaced;
    }

    public static TSLToken fillSingleToken(TSLToken target, Map<String, TSLToken> argumentMap) {
        if (target instanceof TSLCaptureParameter) {
            TSLCaptureParameter parameterToken = (TSLCaptureParameter) target;
            return argumentMap.get(parameterToken.getParameterName());

        } else if (target instanceof TSLGroup) {
            TSLGroup groupToken = (TSLGroup) target;
            String groupExpression = StringUtilities.shrink(groupToken.getRaw(), 1, 1);
            String filled = fillSingleToken(groupExpression, argumentMap);
            List<TSLToken> filledGroupTokens = TSLLexer.lexGroupTokens(filled);
            return new TSLGroup(target.getLine(), target.getCharacter(), filledGroupTokens);

        } else if (target instanceof TSLExpression) {
            TSLExpression expressionToken = (TSLExpression) target;
            String filled = fillSingleToken(expressionToken.getExpression(), argumentMap);
            return new TSLExpression(target.getLine(), target.getCharacter(), filled);
        }

        return target;
    }

    public static String fillSingleToken(String target, Map<String, TSLToken> argumentMap) {
        return PatternUtils.replaceCaptureParams(target, parameterName -> {
            TSLToken argument = argumentMap.get(parameterName);
            if (argument == null) return "{{" + parameterName + "}}";
            return argument.getRaw();
        });
    }

}
