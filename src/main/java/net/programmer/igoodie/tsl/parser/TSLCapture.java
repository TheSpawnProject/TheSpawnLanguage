package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

public class TSLCapture {

    protected String name;
    protected List<TSLToken> capturedTokens;

    public TSLCapture(String name, List<TSLToken> tokens) {
        this.name = name;
        this.capturedTokens = tokens;
    }

    public String getName() {
        return name;
    }

    public List<TSLToken> getCapturedTokens() {
        return capturedTokens;
    }

}
