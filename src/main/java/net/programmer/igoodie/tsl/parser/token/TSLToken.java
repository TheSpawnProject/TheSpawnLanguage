package net.programmer.igoodie.tsl.parser.token;

import net.programmer.igoodie.tsl.parser.tree.TSLParseTreeEntry;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class TSLToken implements TSLParseTreeEntry {

    /**
     * 0-indexed value
     */
    protected int line, col;

    public TSLToken(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    /* --------------------------------------- */

    public Optional<String> getNamespace() {
        return Optional.empty();
    }

    /* --------------------------------------- */

    public abstract @NotNull String getTokenType();

    public abstract @NotNull String getRaw();

    public abstract boolean equalValues(TSLToken otherToken);

    protected <T extends TSLToken> Optional<T> castTokenType(Class<T> type, TSLToken argument) {
        if (!type.isInstance(argument)) return Optional.empty();
        return Optional.of(type.cast(argument));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TSLToken)) return false;
        TSLToken that = (TSLToken) obj;
        return this.equalValues(that)
                && this.line == that.line
                && this.col == that.col;
    }

    /* --------------------------------------- */

    public abstract @NotNull String evaluate(TSLContext context);

    public static List<String> evaluateAll(TSLContext context, List<TSLToken> tokens) {
        return tokens.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.toList());
    }

}
