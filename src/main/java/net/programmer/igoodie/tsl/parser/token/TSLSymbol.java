package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.helper.TextPosition;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.TSLReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TSLSymbol extends TSLToken {

    protected Type symbolType;

    public TSLSymbol(TextPosition beginPos, TextPosition endPos, Type symbolType) {
        super(beginPos, endPos);
        this.symbolType = symbolType;
    }

    @Override
    public @NotNull String getTokenType() {
        return "Symbol";
    }

    @Override
    public @NotNull String getRaw() {
        return symbolType.getSymbol();
    }

    @Override
    public @NotNull String evaluate(TSLContext context) {
        return getRaw();
    }

    @Override
    public boolean equalValues(TSLToken otherToken) {
        return TSLReflectionUtils.castToClass(TSLSymbol.class, otherToken)
                .filter(that -> that.symbolType == this.symbolType)
                .isPresent();
    }

    public enum Type {
        RULESET_TAG_BEGIN("#!"),
        CAPTURE_DECLARATION("="),
        TSLDOC_BEGIN("#**"),
        MULTI_LINE_COMMENT_BEGIN("#*"),
        MULTI_LINE_COMMENT_END("*#"),
        SINGLE_LINE_COMMENT("#");

        private static final Map<String, Type> REGISTRY = new HashMap<>();

        static {
            for (Type type : values()) {
                REGISTRY.put(type.symbol, type);
            }
        }

        public static @Nullable Type bySymbol(String symbol) {
            return REGISTRY.get(symbol);
        }

        private final String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

}
