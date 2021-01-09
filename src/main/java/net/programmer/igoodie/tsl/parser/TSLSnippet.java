package net.programmer.igoodie.tsl.parser;

import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.LinkedList;
import java.util.List;

public class TSLSnippet {

    private Type type;
    private List<TSLToken> tokens;

    public TSLSnippet() {
        this.tokens = new LinkedList<>();
    }

    public void pushToken(TSLToken token) {
        this.tokens.add(token);
    }

    public List<TSLToken> getTokens() {
        return tokens;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TSLSnippet{" +
                "type=" + type +
                ", tokens=" + tokens +
                '}';
    }

    public enum Type {
        TAG,
        CAPTURE,
        RULE
    }

}
