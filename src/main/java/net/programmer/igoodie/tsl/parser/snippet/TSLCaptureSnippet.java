package net.programmer.igoodie.tsl.parser.snippet;

import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureCall;
import net.programmer.igoodie.tsl.parser.token.TSLCaptureParameter;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.util.CollectionUtils;

import java.util.*;

public class TSLCaptureSnippet extends TSLSnippet {

    protected TSLCaptureCall header;
    protected List<TSLToken> capturedTokens;

    public TSLCaptureSnippet(TSLRuleset ruleset, TSLCaptureCall header, List<TSLToken> tokens) {
        super(ruleset, CollectionUtils.asSpreadList(TSLToken.class, header, tokens));
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

    public List<TSLToken> replaceParameters(TSLToken... arguments) {
        return replaceParameters(Arrays.asList(arguments));
    }

    public List<TSLToken> replaceParameters(List<TSLToken> replaceValues) {
        return replaceParameters(argumentsToMap(replaceValues));
    }

    public List<TSLToken> replaceParameters(Map<String, TSLToken> argumentMap) {
        List<TSLToken> replaced = new LinkedList<>();

        for (TSLToken capturedToken : this.capturedTokens) {
            if (capturedToken instanceof TSLCaptureParameter) {
                TSLCaptureParameter parameterToken = (TSLCaptureParameter) capturedToken;
                TSLToken argumentToken = argumentMap.get(parameterToken.getParameterName());
                replaced.add(argumentToken);

            } else if (capturedToken instanceof TSLCaptureCall) {
                TSLCaptureCall captureCall = (TSLCaptureCall) capturedToken;
                if (captureCall.getCaptureName().equals(this.getName())) {
                    throw new TSLRuntimeError("Captures cannot call themselves recursively.", captureCall);
                }
                System.out.println("Replacing " + captureCall);
//                TSLCaptureSnippet referredCapture = captureCall.getReferredCapture(ruleset);
//                replaced.addAll(referredCapture.replaceParameters(TSLTokenizer.tokenizeAll(captureCall)));

            } else {
                replaced.add(capturedToken);
            }
        }

        return replaced;
    }

}
