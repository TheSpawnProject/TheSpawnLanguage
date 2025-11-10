package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.tsl.exception.TSLInternalException;
import net.programmer.igoodie.tsl.runtime.definition.TSLAction;
import net.programmer.igoodie.tsl.runtime.word.TSLWord;
import net.programmer.igoodie.tsl.util.structure.Either;

import java.util.Optional;
import java.util.function.Consumer;

public interface TSLClause {

    default boolean isWord() {
        return this instanceof TSLWord;
    }

    default boolean isAction() {
        return this instanceof TSLAction;
    }

    default TSLWord asWord() {
        return ((TSLWord) this);
    }

    default Optional<TSLWord> getWord() {
        return Optional.of(((TSLWord) this));
    }

    default TSLAction asAction() {
        return ((TSLAction) this);
    }

    default Optional<TSLAction> getAction() {
        return Optional.of(((TSLAction) this));
    }

    default Either<TSLWord, TSLAction> asEither() {
        if (this.isWord()) return Either.left(this.asWord());
        if (this.isAction()) return Either.right(this.asAction());
        throw new TSLInternalException("A clause somehow is neither a word or an action huh?");
    }

    default void ifWord(Consumer<TSLWord> consumer) {
        if (this.isWord()) consumer.accept(this.asWord());
    }

    default void ifAction(Consumer<TSLAction> consumer) {
        if (this.isAction()) consumer.accept(this.asAction());
    }

}
