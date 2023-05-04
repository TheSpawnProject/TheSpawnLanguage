package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.Copyable;
import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TSLDecoratorCall extends TSLToken {

    protected String decoratorName;
    protected List<TSLToken> arguments;

    public TSLDecoratorCall(TextPosition beginPos, TextPosition endPos, String decoratorName) {
        this(beginPos, endPos, decoratorName, Collections.emptyList());
    }

    public TSLDecoratorCall(TextPosition beginPos, TextPosition endPos, String decoratorName, List<TSLToken> arguments) {
        super(beginPos, endPos);
        this.decoratorName = decoratorName;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    @Override
    public TSLDecoratorCall copy() {
        return new TSLDecoratorCall(
                getBeginningPos(),
                getEndingPos(),
                decoratorName,
                Copyable.copyUnmodifiableList(arguments));
    }

    public String getDecoratorName() {
        return decoratorName;
    }

    public List<TSLToken> getArguments() {
        return arguments;
    }

    @Override
    public Optional<String> getNamespace() {
        if (!decoratorName.contains(".")) return Optional.empty();
        return Optional.ofNullable(decoratorName.split("\\.")[0]);
    }

    public String getValue() {
        if (!decoratorName.contains(".")) return decoratorName;
        return decoratorName.split("\\.")[1];
    }

    @Override
    public @NotNull String getTokenType() {
        return "Decorator";
    }

    @Override
    public @NotNull String getRaw() {
        if (arguments.size() == 0) {
            return "@" + decoratorName;
        } else {
            return "@" + decoratorName + "(" + arguments.stream()
                    .map(TSLToken::getRaw)
                    .collect(Collectors.joining(", ")) + ")";
        }
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return TSLReflectionUtils.castToClass(TSLDecoratorCall.class, otherToken)
                .filter(that -> that.decoratorName.equals(this.decoratorName))
                .filter(that -> that.arguments.size() == this.arguments.size())
                .filter(that -> {
                    for (int i = 0; i < that.arguments.size(); i++) {
                        TSLToken thatToken = that.arguments.get(i);
                        TSLToken thisToken = this.arguments.get(i);
                        if (!thatToken.equalValues(thisToken)) return false;
                    }
                    return true;
                })
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        throw new UnsupportedOperationException(getTokenType() + " tokens are not supposed to be evaluated");
    }

}
