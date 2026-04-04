package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Optional;
import java.util.function.Consumer;

public interface TSLClause {

    default boolean isToken() {
        return this instanceof TSLToken;
    }

    @Deprecated(forRemoval = true)
    default boolean isAction() {
        return this instanceof TSLAction;
    }

    default boolean isNest() {
        return this instanceof TSLTokenNest;
    }

    default TSLToken asToken() {
        return ((TSLToken) this);
    }

    default TSLToken expectToken() {
        if (isToken()) return asToken();
        throw new TSLSyntaxException("Expected a single word, found instead -> {}", this);

    }

    default <T extends TSLToken> T expectToken(Class<T> tokenType) {
        TSLToken token = expectToken();
        try {return tokenType.cast(token);} catch (ClassCastException e) {
            throw new TSLSyntaxException("Expected a {}, found instead -> {}", tokenType.getSimpleName(), this);
        }
    }

    default TSLPlainWord expectKeyword(String keyword) {
        if (isToken()) {
            TSLToken token = asToken();
            if (token instanceof TSLPlainWord) {
                TSLPlainWord plainWord = (TSLPlainWord) token;
                if (plainWord.getValue().equalsIgnoreCase(keyword)) {
                    return plainWord;
                }
            }
        }

        throw new TSLSyntaxException("Expected keyword '{}', found instead -> {}", keyword, this);
    }

    default Optional<TSLToken> getToken() {
        return Optional.of(((TSLToken) this));
    }

    default TSLTokenNest asNest() {
        return ((TSLTokenNest) this);
    }

    default TSLTokenNest expectNest() {
        if (isNest()) return asNest();
        throw new TSLSyntaxException("Expected a word nest, found instead -> {}", this);
    }

    default Optional<TSLTokenNest> getNest() {
        return Optional.of(((TSLTokenNest) this));
    }

    default TSLAction expectAction(TSLPlatform platform) {
        TSLTokenNest tokenNest = this.expectNest();
//        new TSLActionInterpreter().
//        tokenNest.get
        // TODO:
        return null;
    }

    @Deprecated(forRemoval = true)
    default TSLAction asAction() {
        return ((TSLAction) this);
    }

    @Deprecated(forRemoval = true)
    default TSLAction expectAction() {
        if (isAction()) return asAction();
        throw new TSLSyntaxException("Expected an action, found a word instead -> {}", this);
    }

    @Deprecated(forRemoval = true)
    default Optional<TSLAction> getAction() {
        return Optional.of(((TSLAction) this));
    }

    default Either<TSLToken, TSLTokenNest> asEither() {
        if (this.isToken()) return Either.left(this.asToken());
        if (this.isNest()) return Either.right(this.asNest());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

    @Deprecated(forRemoval = true)
    default Either<TSLToken, TSLAction> asEither_OLD() {
        if (this.isToken()) return Either.left(this.asToken());
        if (this.isAction()) return Either.right(this.asAction());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

    default void ifToken(Consumer<TSLToken> consumer) {
        if (this.isToken()) consumer.accept(this.asToken());
    }

    default void ifNest(Consumer<TSLTokenNest> consumer) {
        if (this.isToken()) consumer.accept(this.asNest());
    }

    @Deprecated(forRemoval = true)
    default void ifAction(Consumer<TSLAction> consumer) {
        if (this.isAction()) consumer.accept(this.asAction());
    }

}
