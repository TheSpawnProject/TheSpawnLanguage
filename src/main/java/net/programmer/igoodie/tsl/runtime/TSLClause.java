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
        return this instanceof TSLTokenNest;
    }

    default TSLTokenNest asNest() {
        return ((TSLTokenNest) this);
    }

    default TSLTokenNest expectNest() {
        if (isNest()) return asNest();
        throw new TSLSyntaxException("Expected a word nest, found instead -> {}", this);
    }

    default TSLAction expectAction(TSLPlatform platform) {
        TSLTokenNest tokenNest = this.expectNest();

        // TODO: Parse action from tokenNest.getClauses()
        // TODO: Ensure action.parseArguments is called too for the checks

//        new TSLActionInterpreter().
//        tokenNest.get

        // TODO: Return parsed action
        return null;
    }

    default Optional<TSLTokenNest> getNest() {
        return Optional.of(((TSLTokenNest) this));
    }

    default void ifNest(Consumer<TSLTokenNest> consumer) {
        if (this.isToken()) consumer.accept(this.asNest());
    }

    /* ------------------------------------ */

    default Either<TSLToken, TSLTokenNest> asEither() {
        if (this.isToken()) return Either.left(this.asToken());
        if (this.isNest()) return Either.right(this.asNest());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

}
