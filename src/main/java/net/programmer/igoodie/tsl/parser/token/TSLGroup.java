package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TSLGroup extends TSLToken {

    protected List<TSLToken> groupTokens;

    public TSLGroup(int line, int col, List<TSLToken> groupTokens) {
        super(line, col);
        this.groupTokens = groupTokens;
    }

    public List<TSLToken> getGroupTokens() {
        return Collections.unmodifiableList(groupTokens);
    }

    @Override
    public @NotNull String getTokenType() {
        return "Group";
    }

    @Override
    public @NotNull String getRaw() {
        return "%" + reduceGroupTokens(TSLToken::getRaw) + "%";
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return castTokenType(TSLGroup.class, otherToken)
                .filter(that -> that.groupTokens.size() == this.groupTokens.size())
                .filter(that -> that.getRaw().equals(this.getRaw()))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        return reduceGroupTokens(token -> token.evaluate(context));
    }

    protected String reduceGroupTokens(Function<TSLToken, String> reducer) {
        if (groupTokens.size() < 2) {
            return groupTokens.stream()
                    .map(reducer)
                    .collect(Collectors.joining());
        }

        String prevReduction = reducer.apply(groupTokens.get(0));

        StringBuilder builder = new StringBuilder(prevReduction);
        for (int i = 1; i < groupTokens.size(); i++) {
            TSLToken prevToken = groupTokens.get(i - 1);
            TSLToken token = groupTokens.get(i);

            String reduction = reducer.apply(token);

            if (prevToken.line != token.line) {
                builder.append("\n");
            } else {
                int prevEndCol = prevToken.col + prevReduction.length();
                int spaceBetween = token.col - prevEndCol;
                for (int s = 0; s < spaceBetween; s++) {
                    builder.append(" ");
                }
            }

            builder.append(reduction);
            prevReduction = reduction;
        }

        return builder.toString();
    }

}
