package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.context.TSLContext;

public class TSLSymbol extends TSLToken {

    protected Type type;

    public TSLSymbol(int line, int character, Type type) {
        super(line, character);
        this.type = type;
    }

    @Override
    public String getTypeName() {
        return "Symbol";
    }

    @Override
    public String getRaw() {
        return type.getSymbol();
    }

    @Override
    public String evaluate(TSLContext context) {
        return type.getSymbol();
    }

    public Type getType() {
        return type;
    }

    public static boolean equals(TSLToken token, TSLSymbol.Type symbol) {
        return token instanceof TSLSymbol
                && ((TSLSymbol) token).getType() == symbol;
    }

    public enum Type {
        RULESET_TAG_BEGIN("#!"),
        CAPTURE_DECLARATION("="),
        TSLDOC_BEGIN("#**"),
        MULTI_LINE_COMMENT_BEGIN("#*"),
        MULTI_LINE_COMMENT_END("*#"),
        SINGLE_LINE_COMMENT("#");

        private final String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

}
