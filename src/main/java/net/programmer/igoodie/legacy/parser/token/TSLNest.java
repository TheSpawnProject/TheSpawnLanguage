package net.programmer.igoodie.legacy.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.List;
import java.util.stream.Collectors;

public class TSLNest extends TSLToken {

    protected List<TSLToken> tokens;

    public TSLNest(int line, int character, List<TSLToken> tokens) {
        super(line, character);
        this.tokens = tokens;
    }

    public List<TSLToken> getTokens() {
        return tokens;
    }

    public int depth() {
        int depth = 0;
        for (TSLToken token : this.getTokens()) {
            if (token instanceof TSLNest) {
                int childDepth = ((TSLNest) token).depth();
                depth = Math.max(childDepth, depth);
            }
        }
        return depth + 1;
    }

    @Override
    public String getTypeName() {
        return "Nest";
    }

    @Override
    public String getRaw() {
        return "(" + tokens.stream().map(TSLToken::getRaw)
                .collect(Collectors.joining(" ")) + ")";
    }

    @Override
    public String evaluate(TSLContext context) {
        return null; // TODO: Consider usages
    }

}
