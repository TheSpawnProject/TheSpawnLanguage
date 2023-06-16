package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.TextRange;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class TSLPlainWord extends TSLToken {

    protected String plainWord;

    public TSLPlainWord(TextRange range, String plainWord) {
        super(range);
        this.plainWord = plainWord;
    }

    @Override
    public TSLPlainWord copy() {
        return new TSLPlainWord(getRange(), plainWord);
    }

    @Override
    public Optional<String> getNamespace() {
        if (!plainWord.contains(".")) return Optional.empty();
        return Optional.ofNullable(plainWord.split("\\.")[0]);
    }

    public String getValue() {
        if (!plainWord.contains(".")) return plainWord;
        return plainWord.split("\\.")[1];
    }

    @Override
    public @NotNull String getTokenType() {
        return "Plain Word";
    }

    @Override
    public @NotNull String getRaw() {
        return this.plainWord;
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return TSLReflectionUtils.castToClass(TSLPlainWord.class, otherToken)
                .filter(that -> Objects.equals(that.plainWord, this.plainWord))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        return getRaw();
    }

}
