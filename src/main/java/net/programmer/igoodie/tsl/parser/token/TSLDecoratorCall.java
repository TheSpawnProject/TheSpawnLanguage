package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

import java.util.List;

public class TSLDecoratorCall extends TSLToken {

    protected String name;
    protected List<String> args;

    public TSLDecoratorCall(int line, int character, String name, List<String> args) {
        super(line, character);
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String getRaw() {
        StringBuilder builder = new StringBuilder("@");
        builder.append(name);
        if (args.size() != 0) {
            builder.append(String.join(",", args));
        }
        return builder.toString();
    }

    @Override
    public String evaluate(TSLContext context) {
        return getRaw();
    }

}
