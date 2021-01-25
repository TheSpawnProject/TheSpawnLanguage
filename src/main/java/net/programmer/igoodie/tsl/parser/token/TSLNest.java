package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

import java.util.List;
import java.util.stream.Collectors;

public class TSLNest extends TSLToken {

    protected List<TSLToken> tokens;

    public TSLNest(int line, int character, List<TSLToken> tokens) {
        super(line, character);
        this.tokens = tokens;
    }

    @Override
    public String getRaw() {
        return "(" + tokens.stream().map(TSLToken::getRaw)
                .collect(Collectors.joining(" ")) + ")";
    }

    @Override
    public String evaluate(TSLContext context) {
        return null;
    }

}
