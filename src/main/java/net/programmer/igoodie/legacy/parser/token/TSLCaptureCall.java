package net.programmer.igoodie.legacy.parser.token;

import net.programmer.igoodie.legacy.parser.snippet.TSLCaptureSnippet;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TSLCaptureCall extends TSLToken {

    protected String captureName;
    protected List<TSLToken> args;

    public TSLCaptureCall(int line, int character, String captureName) {
        this(line, character, captureName, new LinkedList<>());
    }

    public TSLCaptureCall(int line, int character, String captureName, List<TSLToken> args) {
        super(line, character);
        this.captureName = captureName;
        this.args = args;
    }

    public String getCaptureName() {
        return captureName;
    }

    public List<TSLToken> getArgs() {
        return args;
    }

    @Override
    public String getTypeName() {
        return "Capture Call";
    }

    @Override
    public String getRaw() {
        if (args.size() == 0) {
            return "$" + captureName;
        } else {
            return "$" + captureName + "(" + args.stream()
                    .map(TSLToken::getRaw)
                    .collect(Collectors.joining(", ")) + ")";
        }
    }

    @Override
    public String evaluate(TSLContext context) {
        Map<String, TSLCaptureSnippet> captureSnippets = context.getCaptureSnippets();

        TSLCaptureSnippet associatedCaptureSnippet = captureSnippets.get(getCaptureName());
        if (associatedCaptureSnippet == null) {
            throw new TSLRuntimeError("Unknown capture -> " + getCaptureName(), this);
        }

        List<TSLToken> tokens = associatedCaptureSnippet.fill(captureSnippets, getArgs());
        return tokens.stream().map(token -> token.evaluate(context)).collect(Collectors.joining(" "));
    }

}
