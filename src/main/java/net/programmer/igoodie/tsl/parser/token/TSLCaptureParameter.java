package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

public class TSLCaptureParameter extends TSLToken {

    protected String parameterName;

    public TSLCaptureParameter(int line, int col, String parameterName) {
        super(line, col);
        this.parameterName = parameterName;
    }

    @Override
    public @NotNull String getTokenType() {
        return "Capture Parameter";
    }

    @Override
    public @NotNull String getRaw() {
        return "{{" + parameterName + "}}";
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return castTokenType(TSLCaptureParameter.class, otherToken)
                .filter(that -> that.parameterName.equals(this.parameterName))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        throw new UnsupportedOperationException(getTokenType() + " tokens are not supposed to be evaluated");
    }

}
