package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.TSLTokenizer;
import net.programmer.igoodie.tsl.parser.token.*;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.*;
import java.util.stream.Stream;

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

    public Map<String, TSLToken> argumentsToMap(List<TSLToken> replaceValues) {
        List<String> parameterNames = getParameterNames();
        if (parameterNames.size() > replaceValues.size()) {
            throw new IllegalArgumentException("Insufficient number of arguments...");
        }

        HashMap<String, TSLToken> argumentMap = new HashMap<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            argumentMap.put(parameterNames.get(i), replaceValues.get(i));
        }

        return argumentMap;
    }

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

    public List<TSLToken> flatten(List<TSLToken> replaceValues) {
        return flatten(argumentsToMap(replaceValues));
    }

    public List<TSLToken> flatten(Map<String, TSLToken> argumentMap) {
        List<TSLToken> replaced = new LinkedList<>();

        for (TSLToken capturedToken : this.capturedTokens) {
            if (capturedToken instanceof TSLCaptureParameter) {
                TSLCaptureParameter parameterToken = (TSLCaptureParameter) capturedToken;
                TSLToken argumentToken = argumentMap.get(parameterToken.getParameterName());
                replaced.add(argumentToken);

            } else if (capturedToken instanceof TSLString) {
                TSLString stringToken = (TSLString) capturedToken;
                String replacedString = replaceParameters(stringToken.getWord(), argumentMap);
                replaced.add(new TSLString(stringToken.getLine(), stringToken.getCharacter(), replacedString));

            } else if (capturedToken instanceof TSLCaptureCall) {
                TSLCaptureCall captureCall = (TSLCaptureCall) capturedToken;

                if (captureCall.getCaptureName().equals(this.getName())) {
                    throw new TSLRuntimeError("Captures MUST not call themselves recursively.", captureCall);
                }

                TSLCaptureSnippet captureSnippet = ruleset.getCaptureSnippet(captureCall);


                // TODO:
                // captureSnippet.flatten(captureCall.getArgs());

            } else {
                replaced.add(capturedToken);
            }
        }

        return replaced;
    }

    public static String replaceParameters(String string, Map<String, TSLToken> argumentMap) {
        String replaced = string;
        for (Map.Entry<String, TSLToken> entry : argumentMap.entrySet()) {
            String argumentName = entry.getKey();
            TSLToken argumentValue = entry.getValue();
            replaced = replaced.replaceAll("\\{" + argumentName + "}", argumentValue.getRaw());
        }
        return replaced;
    }

}
