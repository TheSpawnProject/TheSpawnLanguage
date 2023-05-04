package net.programmer.igoodie.tsl.parser.token.base;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class TSLToken {

    protected TextRange range;

    public TSLToken(TextPosition beginPos, TextPosition endPos) {
        this.range = new TextRange(beginPos, endPos);
    }

    public TextRange getRange() {
        return range;
    }

    public TextPosition getBeginningPos() {
        return range.getBeginPos();
    }

    public TextPosition getEndingPos() {
        return range.getEndPos();
    }

    /* --------------------------------------- */

    public Optional<String> getNamespace() {
        return Optional.empty();
    }

    /* --------------------------------------- */

    public abstract @NotNull String getTokenType();

    public abstract @NotNull String getRaw();

    public abstract boolean equalValues(TSLToken otherToken);

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return TSLReflectionUtils.castToClass(TSLToken.class, obj)
                .filter(that -> that.equalValues(this))
                .filter(that -> that.range.equals(this.range))
                .isPresent();
    }

    /* --------------------------------------- */

    public abstract @NotNull String evaluate(TSLContext context);

    public static List<String> evaluateAll(TSLContext context, List<TSLToken> tokens) {
        return tokens.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.toList());
    }

}
