package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TSLCaptureCall extends TSLToken {

    protected String captureName;
    protected List<TSLToken> arguments;

    public TSLCaptureCall(TextPosition beginPos, TextPosition endPos, String captureName) {
        this(beginPos, endPos, captureName, Collections.emptyList());
    }

    public TSLCaptureCall(TextPosition beginPos, TextPosition endPos, String captureName, List<TSLToken> arguments) {
        super(beginPos, endPos);
        this.captureName = captureName;
        this.arguments = arguments;
    }

    public String getCaptureName() {
        return captureName;
    }

    public List<TSLToken> getArguments() {
        return arguments;
    }

    @Override
    public Optional<String> getNamespace() {
        if (!captureName.contains(".")) return Optional.empty();
        return Optional.ofNullable(captureName.split("\\.")[0]);
    }

    public String getValue() {
        if (!captureName.contains(".")) return captureName;
        return captureName.split("\\.")[1];
    }

    @Override
    public @NotNull String getTokenType() {
        return "Capture Call";
    }

    @Override
    public @NotNull String getRaw() {
        if (arguments.size() == 0) {
            return "$" + captureName;
        } else {
            return "$" + captureName + "(" + arguments.stream()
                    .map(TSLToken::getRaw)
                    .collect(Collectors.joining(", ")) + ")";
        }
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return TSLReflectionUtils.castToClass(TSLCaptureCall.class, otherToken)
                .filter(that -> that.captureName.equals(this.captureName))
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
