package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.LinkedList;
import java.util.List;

public class TSLCaptureCall extends TSLToken {

    protected String captureName;
    protected List<String> args;

    public TSLCaptureCall(int line, int character, String captureName) {
        super(line, character);
        this.captureName = captureName;
        this.args = new LinkedList<>();
    }

    public TSLCaptureCall(int line, int character, String captureName, List<String> args) {
        super(line, character);
        this.captureName = captureName;
        this.args = args;
    }

    public String getCaptureName() {
        return captureName;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String getTypeName() {
        return "Capture Call";
    }

    @Override
    public String getRaw() {
        if (args.size() == 0) {
            return "$" + captureName;
        } else {
            return "$" + captureName + "(" + String.join(", ", args) + ")";
        }
    }

    @Override
    public String evaluate(TSLContext context) {
        throw new UnsupportedOperationException();
    }

}
