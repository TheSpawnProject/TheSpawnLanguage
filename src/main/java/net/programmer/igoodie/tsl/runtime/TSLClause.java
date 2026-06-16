package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.token.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.token.TSLToken;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Optional;
import java.util.function.Consumer;

public interface TSLClause {

    default boolean isToken() {
        return this instanceof TSLToken;
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
        try {
            return tokenType.cast(token);
        } catch (ClassCastException e) {
            throw new TSLSyntaxException("Expected a {}, found instead -> {}", tokenType.getSimpleName(), this);
        }
    }

    default TSLPlainWord expectKeyword(String keyword) {
        TSLPlainWord word = this.expectToken(TSLPlainWord.class);

        if (word.getValue().equalsIgnoreCase(keyword)) {
            return word;
        }

        throw new TSLSyntaxException("Expected keyword '{}', found instead -> {}", keyword, this);
    }

    default Optional<TSLToken> getToken() {
        return Optional.of(((TSLToken) this));
    }

    default void ifToken(Consumer<TSLToken> consumer) {
        if (this.isToken()) consumer.accept(this.asToken());
    }

    /* ------------------------------------ */

    default boolean isNest() {
        return this instanceof TSLActionNest;
    }

    default TSLActionNest asNest() {
        return ((TSLActionNest) this);
    }

    default TSLActionNest expectNest() {
        if (isNest()) return asNest();
        throw new TSLSyntaxException("Expected a word nest, found instead -> {}", this);
    }

    default Optional<TSLActionNest> getNest() {
        return Optional.of(((TSLActionNest) this));
    }

    default void ifNest(Consumer<TSLActionNest> consumer) {
        if (this.isToken()) consumer.accept(this.asNest());
    }

    /* ------------------------------------ */

    default Either<TSLToken, TSLActionNest> asEither() {
        if (this.isToken()) return Either.left(this.asToken());
        if (this.isNest()) return Either.right(this.asNest());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

    /* ------------------------------------ */

    String toDebugString();

}
