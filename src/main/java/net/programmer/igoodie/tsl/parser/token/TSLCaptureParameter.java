package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.parser.token.base.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;

public class TSLCaptureParameter extends TSLToken {

    protected String parameterName;

    public TSLCaptureParameter(TextPosition beginPos, TextPosition endPos, String parameterName) {
        super(beginPos, endPos);
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
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
        return TSLReflectionUtils.castToClass(TSLCaptureParameter.class, otherToken)
                .filter(that -> that.parameterName.equals(this.parameterName))
                .isPresent();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        throw new UnsupportedOperationException(getTokenType() + " tokens are not supposed to be evaluated");
    }

}
