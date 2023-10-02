package net.programmer.igoodie.legacy.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;

@Deprecated
public class TSLCaptureParameter extends TSLToken {

    protected String parameterName;

    public TSLCaptureParameter(int line, int character, String parameterName) {
        super(line, character);
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    @Override
    public String getTypeName() {
        return "Capture Parameter";
    }

    @Override
    public String getRaw() {
        return "{{" + parameterName + "}}";
    }

    @Override
    public String evaluate(TSLContext context) {
        throw new UnsupportedOperationException();
    }

}
