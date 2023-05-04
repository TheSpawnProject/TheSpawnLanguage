package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TSLGroup extends TSLToken {

    protected List<TSLToken> groupedTokens;
    protected List<String> whitespaces;

    public TSLGroup(TextPosition beginPos, TextPosition endPos, List<TSLToken> groupTokens) {
        super(beginPos, endPos);
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
        return TSLReflectionUtils.castToClass(TSLGroup.class, otherToken)
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

        if (groupedTokens.size() == 0) {
            whitespaces.add(whitespacesBetween(
                    this.getBeginningPos(),
                    this.getEndingPos(),
                    false));
            return;
        }

        // Whitespaces between group begin and first token
        whitespaces.add(whitespacesBetween(
                this.getBeginningPos(),
                groupedTokens.get(0).getBeginningPos(),
                false
        ));

        // Whitespaces between tokens
        for (int i = 1; i < groupedTokens.size(); i++) {
            TSLToken prevToken = groupedTokens.get(i - 1);
            TSLToken token = groupedTokens.get(i);
            whitespaces.add(whitespacesBetween(
                    prevToken.getEndingPos(),
                    token.getBeginningPos(),
                    true));
        }

        // Whitespaces between last token and group end
        whitespaces.add(whitespacesBetween(
                groupedTokens.get(groupedTokens.size() - 1).getEndingPos(),
                this.getEndingPos(),
                false
        ));
    }

    protected String whitespacesBetween(TextPosition begin, TextPosition end, boolean onlyOneType) {
        int linesBetween = end.getLine() - begin.getLine();
        int spacesBetween = end.getCol() - begin.getCol() - 1;
        StringBuilder whitespace = new StringBuilder();
        for (int s = 0; s < linesBetween; s++) whitespace.append("\n");
        if (!onlyOneType || whitespace.length() == 0)
            for (int s = 0; s < spacesBetween; s++) whitespace.append(" ");
        return whitespace.toString();
    }

    protected String reduceGroupedTokens(Function<TSLToken, String> reducer) {
        if (groupedTokens.size() == 0) return whitespaces.get(0);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < whitespaces.size() - 1; i++) {
            builder.append(whitespaces.get(i));
            builder.append(reducer.apply(groupedTokens.get(i)));
        }

        builder.append(whitespaces.get(whitespaces.size() - 1));

        return builder.toString();
    }

}
