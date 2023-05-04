package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TSLCaptureCall extends TSLToken {

    protected String captureName;
    protected List<TSLToken> arguments;

    public TSLCaptureCall(int line, int col, String captureName) {
        this(line, col, captureName, Collections.emptyList());
    }

    public TSLCaptureCall(int line, int col, String captureName, List<TSLToken> arguments) {
        super(line, col);
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
        return castTokenType(TSLCaptureCall.class, otherToken)
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
