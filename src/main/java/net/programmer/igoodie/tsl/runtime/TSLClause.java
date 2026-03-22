package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLPlainWord;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Optional;
import java.util.function.Consumer;

public interface TSLClause {

    default boolean isWord() {
        return this instanceof TSLWord;
    }

    @Deprecated(forRemoval = true)
    default boolean isAction() {
        return this instanceof TSLAction;
    }

    default boolean isNest() {
        return this instanceof TSLWordNest;
    }

    default TSLWord asWord() {
        return ((TSLWord) this);
    }

    default TSLWord expectWord() {
        if (isWord()) return asWord();
        throw new TSLSyntaxException("Expected a single word, found instead -> {}", this);

    }

    default TSLPlainWord expectKeyword(String keyword) {
        if (isWord()) {
            TSLWord word = asWord();
            if (word instanceof TSLPlainWord) {
                TSLPlainWord plainWord = (TSLPlainWord) word;
                if (plainWord.getValue().equalsIgnoreCase(keyword)) {
                    return plainWord;
                }
            }
        }

        throw new TSLSyntaxException("Expected keyword '{}', found instead -> {}", keyword, this);
    }

    default Optional<TSLWord> getWord() {
        return Optional.of(((TSLWord) this));
    }

    default TSLWordNest asNest() {
        return ((TSLWordNest) this);
    }

    default TSLWordNest expectNest() {
        if (isNest()) return asNest();
        throw new TSLSyntaxException("Expected a word nest, found instead -> {}", this);
    }

    default Optional<TSLWordNest> getNest() {
        return Optional.of(((TSLWordNest) this));
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

    default Either<TSLWord, TSLWordNest> asEither() {
        if (this.isWord()) return Either.left(this.asWord());
        if (this.isNest()) return Either.right(this.asNest());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

    @Deprecated(forRemoval = true)
    default Either<TSLWord, TSLAction> asEither_OLD() {
        if (this.isWord()) return Either.left(this.asWord());
        if (this.isAction()) return Either.right(this.asAction());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

    default void ifWord(Consumer<TSLWord> consumer) {
        if (this.isWord()) consumer.accept(this.asWord());
    }

    @Deprecated(forRemoval = true)
    default void ifAction(Consumer<TSLAction> consumer) {
        if (this.isAction()) consumer.accept(this.asAction());
    }

}
