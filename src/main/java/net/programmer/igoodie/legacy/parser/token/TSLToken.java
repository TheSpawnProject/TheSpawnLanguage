package net.programmer.igoodie.legacy.parser.token;

import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public abstract class TSLToken {

    protected int line, character;

    public TSLToken(int line, int character) {
        this.line = line;
        this.character = character;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    public abstract String getTypeName();

    public abstract String getRaw();

    public abstract String evaluate(TSLContext context);

    public boolean hasNamespace() {
        return getNamespace() != null;
    }

    public @Nullable String getNamespace() {
        return null;
    }

    public boolean isTrue(TSLContext context) {
        String evaluation = evaluate(context);
        return evaluation.equalsIgnoreCase("TRUE") || evaluation.equals("1");
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), getRaw());
    }

    /* ---------------------------- */

    public boolean isCaptureCall() {
        return this instanceof TSLCaptureCall;
    }

    public boolean isCaptureParameter() {
        return this instanceof TSLCaptureParameter;
    }

    public boolean isDecoratorCall() {
        return this instanceof TSLDecoratorCall;
    }

    public boolean isExpression() {
        return this instanceof TSLExpression;
    }

    public boolean isGroup() {
        return this instanceof TSLGroup;
    }

    public boolean isNest() {
        return this instanceof TSLNest;
    }

    public boolean isPlainWord() {
        return this instanceof TSLPlainWord;
    }

    public boolean isSymbol() {
        return this instanceof TSLSymbol;
    }

    /* ---------------------------- */

    public static List<String> evaluateAll(TSLContext context, List<TSLToken> tokens) {
        return tokens.stream()
                .map(token -> token.evaluate(context))
                .collect(Collectors.toList());
    }

}
