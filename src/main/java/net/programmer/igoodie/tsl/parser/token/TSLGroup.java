package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TSLGroup extends TSLToken {

    protected List<TSLToken> groupedTokens;
    protected List<String> whitespaces;

    public TSLGroup(int line, int col, List<TSLToken> groupTokens) {
        super(line, col);
        this.groupedTokens = groupTokens;
        calcWhitespaces();
    }

    public List<TSLToken> getGroupedTokens() {
        return Collections.unmodifiableList(groupedTokens);
    }

    @Override
    public @NotNull String getTokenType() {
        return "Group";
    }

    @Override
    public @NotNull String getRaw() {
        return "%" + reduceGroupedTokens(TSLToken::getRaw) + "%";
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return castTokenType(TSLGroup.class, otherToken)
                .filter(that -> that.groupedTokens.size() == this.groupedTokens.size())
                .filter(that -> that.getRaw().equals(this.getRaw()))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        return reduceGroupedTokens(token -> token.evaluate(context));
    }

    protected void calcWhitespaces() {
        this.whitespaces = new ArrayList<>();

        if (groupedTokens.size() < 2)
            return;

        String prevTokenRaw = groupedTokens.get(0).getRaw();

        for (int i = 1; i < groupedTokens.size(); i++) {
            TSLToken prevToken = groupedTokens.get(i - 1);
            TSLToken token = groupedTokens.get(i);

            String tokenRaw = token.getRaw();

            if (prevToken.line != token.line) {
                whitespaces.add("\n");
            } else {
                int prevEndCol = prevToken.col + prevTokenRaw.length();
                int spaceBetween = token.col - prevEndCol;
                StringBuilder whitespace = new StringBuilder();
                for (int s = 0; s < spaceBetween; s++) whitespace.append(" ");
                whitespaces.add(whitespace.toString());
            }

            prevTokenRaw = tokenRaw;
        }
    }

    protected String reduceGroupedTokens(Function<TSLToken, String> reducer) {
        if (groupedTokens.size() == 0) return "";
        if (groupedTokens.size() == 1) return reducer.apply(groupedTokens.get(0));

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < groupedTokens.size(); i++) {
            TSLToken token = groupedTokens.get(i);
            builder.append(reducer.apply(token));

            if (i != groupedTokens.size() - 1) {
                String whitespace = whitespaces.get(i);
                builder.append(whitespace);
            }
        }

        return builder.toString();
    }

}
