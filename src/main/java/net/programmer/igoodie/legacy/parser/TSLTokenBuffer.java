package net.programmer.igoodie.legacy.parser;

import net.programmer.igoodie.legacy.parser.token.TSLToken;

import java.util.LinkedList;
import java.util.List;

public final class TSLTokenBuffer {

    public enum Type {
        TAG,
        CAPTURE,
        RULE,
        COMMENT,
        TSLDOC
    }

    private Type type;
    private final List<TSLToken> tokens;

    public TSLTokenBuffer() {
        this.tokens = new LinkedList<>();
    }

    public void pushToken(TSLToken token) {
        this.tokens.add(token);
    }

    public List<TSLToken> getTokens() {
        return tokens;
    }

    public int getBeginningLine() {
        if (tokens.size() == 0) return -1;
        return tokens.get(0).getLine();
    }

    public int getEndingLine() {
        if (tokens.size() == 0) return -1;
        return tokens.get(tokens.size() - 1).getLine();
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

}
