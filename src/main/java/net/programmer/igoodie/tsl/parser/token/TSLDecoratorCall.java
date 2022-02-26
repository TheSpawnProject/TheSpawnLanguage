package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TSLDecoratorCall extends TSLToken {

    protected String name;
    protected List<String> args;

    public TSLDecoratorCall(int line, int character, String name, List<String> args) {
        super(line, character);
        this.name = name;
        this.args = args;
    }

    @Nullable
    public String getNamespace() {
        if (!name.contains(".")) return null;
        return name.split("\\.")[0];
    }

    public String getName() {
        if (!name.contains(".")) return name;
        return name.split("\\.")[1];
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String getTypeName() {
        return "Decorator";
    }

    @Override
    public String getRaw() {
        StringBuilder builder = new StringBuilder("@");
        builder.append(name);
        if (args.size() != 0) {
            builder.append("(").append(String.join(", ", args)).append(")");
        }
        return builder.toString();
    }

    @Override
    public String evaluate(TSLContext context) {
        return getRaw();
    }

}
